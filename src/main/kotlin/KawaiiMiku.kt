package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.internal.spi.EncryptService
import kotlin.reflect.KClass

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
        val factory: KClass<*>
        if (ServiceConfig.legacy) {
            factory = EncryptProviderLegacy::class
            EncryptProviderLegacy.Factory.put(ServiceConfig.serviceUrl, ServiceConfig.serviceKey, ServiceConfig.legacyQUA)
            EncryptProviderLegacy.Factory.registerAsOverride()
        } else {
            factory = EncryptProvider::class
            EncryptProvider.Factory.put(ServiceConfig.serviceUrl, ServiceConfig.serviceKey)
            EncryptProvider.Factory.registerAsOverride()
        }
        logger.warning("若使用本插件，即代表你信任本插件获取 qmiei、guid 等信息用于调用签名服务")
        logger.warning("警告为信息安全相关的警告，如果你信任本插件，可忽略该警告")
        @Suppress("INVISIBLE_MEMBER")
        EncryptService.factory

        logger.info("Registered service: ${factory.qualifiedName}")
    }
    override fun onEnable() {
        logger.info("Plugin enabled")
    }
}
