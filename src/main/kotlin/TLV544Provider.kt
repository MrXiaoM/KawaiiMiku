package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.internal.spi.EncryptServiceContext
import net.mamoe.mirai.internal.spi.EncryptServiceContext.Companion.KEY_COMMAND_STR

class TLV544Provider : EncryptService {
    override fun encryptTlv(
        context: EncryptServiceContext,
        tlvType: Int,
        payload: ByteArray
    ): ByteArray? {
        if (tlvType == 0x544) {
            when (context.extraArgs[KEY_COMMAND_STR]) {
                "810_a" -> {}
                "810_f" -> {}
                "810_2" -> {}
                "810_7" -> {}
                "810_9" -> {}
            }
        }
        return null
    }
}