package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.Services
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
    override fun PluginComponentStorage.onLoad() {
       Services.register("net.mamoe.mirai.internal.spi.EncryptService",
           "top.mrxiaom.mirai.kawaii.TLV544Provider") {
           return@register TLV544Provider
       }
        logger.info("Registered service: TLV544Provider")
    }
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}