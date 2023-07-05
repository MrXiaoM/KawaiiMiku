package top.mrxiaom.mirai.kawaii

import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_BOT_PROTOCOL
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_CHANNEL_PROXY
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_COMMAND_STR
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_DEVICE_INFO
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_QIMEI36
import net.mamoe.mirai.utils.*
import kotlin.coroutines.CoroutineContext

class EncryptProvider(
    server: String,
    key: String,
    coroutineContext: CoroutineContext
) : EncryptService, SignClient(server, key), CoroutineScope {
    override val coroutineContext: CoroutineContext =
        coroutineContext + SupervisorJob(coroutineContext[Job]) + CoroutineExceptionHandler { context, exception ->
            when (exception) {
                is CancellationException -> Unit
                else -> {
                    logger.warning("with ${context[CoroutineName]}", exception)
                }
            }
        }
    private var channel0: EncryptService.ChannelProxy? = null
    private val channel: EncryptService.ChannelProxy get() = channel0 ?: throw IllegalStateException("need initialize")
    private val token = java.util.concurrent.atomic.AtomicBoolean(false)

    @Deprecated(
        message = "",
        replaceWith = ReplaceWith(
            "EncryptProvider.Factory(url, key).register()",
            "top.mrxiaom.mirai.kawaii.EncryptProvider.Factory"
        )
    )
    fun register() {
        Services.register(
            EncryptService::class.qualifiedName!!,
            this::class.qualifiedName!!) { this }
    }
    @Deprecated(
        message = "",
        replaceWith = ReplaceWith(
            "EncryptProvider.Factory(url, key).registerAsOverride()",
            "top.mrxiaom.mirai.kawaii.EncryptProvider.Factory"
        )
    )
    fun registerAsOverride() {
        Services.registerAsOverride(
            EncryptService::class.qualifiedName!!,
            this::class.qualifiedName!!) { this }
    }

    @OptIn(MiraiInternalApi::class)
    override fun initialize(context: EncryptServiceContext) {
        when (context.extraArgs[KEY_BOT_PROTOCOL]) {
            BotConfiguration.MiraiProtocol.ANDROID_PHONE -> Unit
            BotConfiguration.MiraiProtocol.ANDROID_PAD -> Unit
            else -> return
        }
        val deviceInfo = context.extraArgs[KEY_DEVICE_INFO]
        channel0 = context.extraArgs[KEY_CHANNEL_PROXY]

        get("register",
            "uin" to context.id,
            "android_id" to deviceInfo.androidId.decodeToString(),
            "guid" to deviceInfo.guid.toUHexString(""),
            "qimei36" to context.extraArgs[KEY_QIMEI36],
            "key" to key
        )
    }

    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType != 0x544) return null
        val command = context.extraArgs[KEY_COMMAND_STR]

        val energy = get("custom_energy",
            "uin" to context.id,
            "salt" to payload.toUHexString(""),
            "data" to command
        ) ?: return null

        val data = Json.decodeFromJsonElement(String.serializer(), energy)
        logger.verbose("encryptTlv $data")

        return data.hexToBytes()
    }

    override fun qSecurityGetSign(
        context: EncryptServiceContext,
        sequenceId: Int,
        commandName: String,
        payload: ByteArray
    ): EncryptService.SignResult? {
        if (commandName == "StatSvc.register") {
            if (!token.get() && token.compareAndSet(false, true)) {
                launch(CoroutineName("RequestToken")) {
                    get("request_token", "uin" to context.id)
                    while (token.get()) {
                        delay((30 .. 40).random() * 60_000L)

                        get("request_token", "uin" to context.id)
                    }
                }
            }
        }

        if (commandName !in ServiceConfig.serviceCmdWhiteList) return null

        val data = Json.decodeFromJsonElement<SignResult>(post("sign",
            "uin" to context.id,
            "cmd" to commandName,
            "seq" to sequenceId,
            "buffer" to payload.toUHexString("")
        ) ?: return null)

        launch(CoroutineName("SendMessage")) {
            for (callback in data.requestCallback) {
                logger.verbose("Bot(${context.id}) sendMessage ${callback.cmd} ")
                val result = channel.sendMessage(
                    remark = "mobileqq.msf.security",
                    commandName = callback.cmd,
                    uin = context.id,
                    data = callback.body.hexToBytes()
                )
                if (result == null) {
                    logger.debug("${callback.cmd} ChannelResult is null")
                    continue
                }

                get("submit",
                    "uin" to context.id,
                    "cmd" to result.cmd,
                    "callbackId" to callback.id,
                    "buffer" to result.data.toUHexString())
            }
        }

        return EncryptService.SignResult(
            sign = data.sign.hexToBytes(),
            token = data.token.hexToBytes(),
            extra = data.extra.hexToBytes(),
        )
    }
    companion object {
        val logger: MiraiLogger = MiraiLogger.Factory.create(EncryptProvider::class)
    }

    object Factory : EncryptService.Factory {
        private lateinit var url: String
        private lateinit var key: String
        var supportedProtocol: Array<BotConfiguration.MiraiProtocol> = arrayOf(
            BotConfiguration.MiraiProtocol.ANDROID_PHONE,
            BotConfiguration.MiraiProtocol.ANDROID_PAD
        )
        fun put(url: String, key: String) {
            this.url = url
            this.key = key
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
            if (context.extraArgs[KEY_BOT_PROTOCOL] in supportedProtocol) {
                return EncryptProvider(url, key, serviceSubScope.coroutineContext)
            }
            throw UnsupportedOperationException()
        }
    }
}

@Serializable
private data class SignResult(
    @SerialName("token")
    val token: String = "",
    @SerialName("extra")
    val extra: String = "",
    @SerialName("sign")
    val sign: String = "",
    @SerialName("o3did")
    val o3did: String = "",
    @SerialName("requestCallback")
    val requestCallback: List<RequestCallback> = emptyList()
)

@Serializable
private data class RequestCallback(
    @SerialName("body")
    val body: String = "",
    @SerialName("callbackId")
    val id: Int = 0,
    @SerialName("cmd")
    val cmd: String = ""
)
