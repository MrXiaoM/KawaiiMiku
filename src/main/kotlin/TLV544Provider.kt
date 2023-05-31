package top.mrxiaom.mirai.kawaii

import kotlinx.serialization.json.*
import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_COMMAND_STR
import net.mamoe.mirai.utils.*
import java.net.URL
import kotlin.io.use

object TLV544Provider : EncryptService {
    private val dataForVerify = arrayOf("810_2", "810_7")
    // 暂不确定 810_25 的 salt 算法类型
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
}