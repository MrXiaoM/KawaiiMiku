package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.utils.Services
import kotlin.reflect.jvm.jvmName

object EncryptProvider : EncryptService {
    fun register() {
        Services.register(
            EncryptService::class.jvmName,
            this::class.jvmName) { this }
    }
    fun registerAsOverride() {
        Services.registerAsOverride(
            EncryptService::class.jvmName,
            this::class.jvmName) { this }
    }

    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType != 0x544) return null
        val command = context.extraArgs[EncryptServiceContext.KEY_COMMAND_STR]

        KawaiiMiku.logger.info("t544 command: $command")

        TODO("Not yet implemented")
    }

    override fun initialize(context: EncryptServiceContext) {
        TODO("Not yet implemented")
    }

    override fun qSecurityGetSign(
        context: EncryptServiceContext,
        sequenceId: Int,
        commandName: String,
        payload: ByteArray
    ): EncryptService.SignResult? {
        TODO("Not yet implemented")
    }
}
