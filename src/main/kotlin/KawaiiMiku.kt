package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import kotlin.reflect.jvm.jvmName

object KawaiiMiku : KotlinPlugin(
    JvmPluginDescription(
        id = "top.mrxiaom.mirai.kawaii",
        name = "KawaiiMiku",
        version = BuildConstants.VERSION,
    ) {
        author("MrXiaoM")
    }
) {
    override fun PluginComponentStorage.onLoad() {
        EncryptProvider.registerAsOverride()
        logger.info("Registered service: ${EncryptProvider::class.jvmName}")
    }
    override fun onEnable() {
        ServiceConfig.reload()
        KawaiiCommand.register()
        logger.info("Plugin enabled")
    }
}