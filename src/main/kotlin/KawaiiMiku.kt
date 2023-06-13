package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.Services
import net.mamoe.mirai.utils.info
import kotlin.reflect.jvm.jvmName

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
        Services.register("net.mamoe.mirai.internal.spi.EncryptService", EncryptProvider::class.jvmName) { EncryptProvider }
        logger.info("Registered service: ${EncryptProvider::class.jvmName}")
    }
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}