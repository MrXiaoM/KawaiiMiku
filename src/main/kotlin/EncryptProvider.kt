package top.mrxiaom.mirai.kawaii

import kotlinx.serialization.json.*
import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_BOT_DEVICE
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_BOT_PROTOCOL
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_BOT_QIMEI16
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_COMMAND_STR
import net.mamoe.mirai.internal.spi.SignResult
import net.mamoe.mirai.utils.*
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.NameValuePair
import org.apache.hc.core5.http.io.entity.EntityUtils
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import kotlin.io.use

object EncryptProvider : EncryptService {
    private val dataForVerify = arrayOf("810_2", "810_7")
    // 暂不确定 810_2 的 salt 算法类型
    private val saltV1 = arrayOf("810_25", "810_7", "810_24")
    private val saltV2 = arrayOf("810_f", "810_9", "810_a", "810_d")
    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType == 0x544) {
            val data = context.extraArgs[KEY_COMMAND_STR]
            val isForVerify = dataForVerify.contains(data)
            val saltMode = when {
                saltV1.contains(data) -> "v1"
                saltV2.contains(data) -> "v2"
                else -> null
            }
            val guid: String
            val version: String
            payload.toReadPacket().use { dataIn ->
                if (isForVerify) Ktor.readLong(dataIn) else dataIn.readPacketExact(4)
                guid = dataIn.readUShortLVByteArray().toUHexString("")
                version = dataIn.readUShortLVString()
            }
            KawaiiMiku.logger.debug("Fetching from web API: tlv544 for ${if(isForVerify) "Verify" else "Token"} ($data), sdkVersion: $version, guid(${guid.length}): $guid, saltMode: ${saltMode ?: "auto"}")
            val conn = URL(
                arrayOf(
                    ProviderConfig.apiUrl.removeSuffix("/"),
                    "/energy",
                    "?name=",
                    ProviderConfig.apiVersion,
                    "&version=$version",
                    "&uin=${context.id}",
                    "&guid=$guid",
                    "&data=$data",
                    saltMode?.run { "&mode=$this" } ?: ""
                ).joinToString("")
            ).openConnection()
            conn.connect()
            val dl = conn.getInputStream().use { it.readBytes() }.toString(Charsets.UTF_8)
            KawaiiMiku.logger.debug("Result: $dl")
            val json = Json.parseToJsonElement(dl).jsonObject
            val code = json["code"]?.jsonPrimitive?.intOrNull ?: -1
            val result = json["data"]?.jsonPrimitive?.contentOrNull
            if (code != 0) return null
            return result?.run {
                chunked(2).map { it.toInt(32).toByte() }.toByteArray()
            } ?: payload
        }
        return payload
    }

    override fun doSecSign(
        context: EncryptServiceContext,
        sequenceId: Int,
        body: ByteArray
    ): SignResult? {
        val uin = context.id
        val commandName = context.extraArgs.get(KEY_COMMAND_STR)
        val androidId = context.extraArgs.get(KEY_BOT_DEVICE).androidId
        val qimei16 = context.extraArgs.get(KEY_BOT_QIMEI16)?: return null
        val protocol = context.extraArgs.get(KEY_BOT_PROTOCOL)

        val client = HttpClients.createDefault()
        val httpPost = HttpPost(ProviderConfig.apiUrl.removeSuffix("/") + "/sign")
        val params = mapOf(
            "name" to ProviderConfig.apiVersionSign,
            "qua" to ProviderConfig.qua[protocol.name.uppercase()],
            "uin" to uin,
            "buffer" to body.toUHexString(""),
            "cmd" to commandName,
            "seq" to sequenceId,
            "androidId" to androidId.toUHexString(),
            "qimei16" to qimei16
        ).map { NameValue(it.key, it.value.toString()) }
        httpPost.entity = UrlEncodedFormEntity(params, Charsets.UTF_8)
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded")
        return client.execute(httpPost) {
            if (it.code != HTTP_OK) return@execute null
            val msg = EntityUtils.toString(it.entity)
            val json = Json.parseToJsonElement(msg).jsonObject
            if ((json["code"]?.jsonPrimitive?.intOrNull ?: -1) != 0) return@execute null
            val data = json["data"]?.jsonObject ?: return@execute null
            val token = data["token"]?.hexToBytes() ?: return@execute null
            val extra = data["extra"]?.hexToBytes() ?: return@execute null
            val sign = data["sign"]?.hexToBytes() ?: return@execute null
            SignResult(qimei16.hexToBytes(), sign, token, extra)
        }
    }
}
fun JsonElement.hexToBytes(): ByteArray = jsonPrimitive.content.hexToBytes()
class NameValue(
    private val name: String,
    private val value: String
): NameValuePair {
    override fun getName(): String = name
    override fun getValue(): String = value
}
