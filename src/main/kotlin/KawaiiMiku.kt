package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

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
        ServiceConfig.reload()
        EncryptProvider.Factory(ServiceConfig.serviceUrl, ServiceConfig.serviceKey).registerAsOverride()
        logger.info("Registered service: ${EncryptProvider.Factory::class.qualifiedName}")
    }
    override fun onEnable() {
        logger.info("Plugin enabled")
    }
}
