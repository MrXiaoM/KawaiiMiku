package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext

object EncryptProvider : EncryptService {
    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType != 0x544) return null
        val command = context.extraArgs[EncryptServiceContext.KEY_COMMAND_STR]

        KawaiiMiku.logger.info("t544 command: $command")

        return null
    }
}
