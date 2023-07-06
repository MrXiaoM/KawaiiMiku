package top.mrxiaom.mirai.kawaii

import kotlinx.coroutines.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.utils.*

class EncryptProviderLegacy : EncryptService, SignClient() {
    override val url: String
        get() = Factory.url
    override val key: String
        get() = Factory.key
    private lateinit var qimei36: String
    override fun initialize(context: EncryptServiceContext) {
        when (context.extraArgs[EncryptServiceContext.KEY_BOT_PROTOCOL]) {
            BotConfiguration.MiraiProtocol.ANDROID_PHONE -> Unit
            BotConfiguration.MiraiProtocol.ANDROID_PAD -> Unit
            else -> return
        }
        qimei36 = context.extraArgs[EncryptServiceContext.KEY_QIMEI36]

    }

    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType != 0x544) return null
        val command = context.extraArgs[EncryptServiceContext.KEY_COMMAND_STR]

        val energy = get("custom_energy",
            "uin" to context.id,
            "salt" to payload.toUHexString(""),
            "data" to command
        ) ?: return null

        val data = Json.decodeFromJsonElement(String.serializer(), energy)
        logger.verbose("custom_energy $command $data")

        return data.hexToBytes()
    }

    override fun qSecurityGetSign(
        context: EncryptServiceContext,
        sequenceId: Int,
        commandName: String,
        payload: ByteArray
    ): EncryptService.SignResult? {

        if (commandName !in Factory.cmdWhiteList) return null

        val data = Json.decodeFromJsonElement<SignResult>(
            post(
                "sign",
                "uin" to context.id,
                "qua" to Factory.qua,
                "cmd" to commandName,
                "seq" to sequenceId,
                "buffer" to payload.toUHexString(""),
                "qimei36" to qimei36
            ) ?: return null
        )

        return EncryptService.SignResult(
            sign = data.sign.hexToBytes(),
            token = data.token.hexToBytes(),
            extra = data.extra.hexToBytes(),
        )
    }
    companion object {
        val logger: MiraiLogger = MiraiLogger.Factory.create(EncryptProviderLegacy::class)
    }

    object Factory : EncryptService.Factory {
        internal lateinit var url: String
        internal lateinit var key: String
        internal lateinit var qua: String
        var cmdWhiteList: List<String> = listOf()
        var supportedProtocol: Array<BotConfiguration.MiraiProtocol> = arrayOf(
            BotConfiguration.MiraiProtocol.ANDROID_PHONE,
            BotConfiguration.MiraiProtocol.ANDROID_PAD
        )
        fun put(url: String, key: String, qua: String) {
            this.url = url
            this.key = key
            this.qua = qua
        }
        fun register() {
            Services.register(
                EncryptService.Factory::class.qualifiedName!!,
                this::class.qualifiedName!!
            ) { this }
        }

        fun registerAsOverride() {
            Services.registerAsOverride(
                EncryptService.Factory::class.qualifiedName!!,
                this::class.qualifiedName!!
            ) { this }
        }

        override fun createForBot(context: EncryptServiceContext, serviceSubScope: CoroutineScope): EncryptService {
            if (context.extraArgs[EncryptServiceContext.KEY_BOT_PROTOCOL] in supportedProtocol) {
                return EncryptProviderLegacy()
            }
            throw UnsupportedOperationException()
        }
    }
}