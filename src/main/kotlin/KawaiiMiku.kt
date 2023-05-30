package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.internal.spi.EncryptService
import net.mamoe.mirai.utils.info

object KawaiiMiku : KotlinPlugin(
    JvmPluginDescription(
        id = "top.mrxiaom.mirai.kawaii",
        name = "KawaiiMiku",
        version = "0.1.0",
    ) {
        author("MrXiaoM")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}