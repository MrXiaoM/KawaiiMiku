@file:Suppress("EXPOSED_SUPER_CLASS", "INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package top.mrxiaom.mirai.kawaii

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.descriptor.CommandValueArgumentParser
import net.mamoe.mirai.console.command.descriptor.InternalCommandValueArgumentParserExtensions
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.YamlDynamicSerializer
import java.net.URLEncoder

object ServiceConfig : ReadOnlyPluginConfig("config") {
    @Serializable
    data class Account(
        @Comment("QQ账号")
        val account: String,
        @Comment(
            """
            账号配置. 可用配置列表 (注意大小写):
            "androidId": "xxxxxxxxxxxxxxxx"
        """
        )
        val configuration: Map<ConfigurationKey, @Serializable(with = YamlDynamicSerializer::class) Any> = mapOf(
            ConfigurationKey.androidId to "xxxxxxxxxxxxxxxx"
        ),
    ) {
        @Serializable
        @Suppress("EnumEntryName")
        enum class ConfigurationKey {
            androidId,
            ;

            object Parser : CommandValueArgumentParser<ConfigurationKey>,
                InternalCommandValueArgumentParserExtensions<ConfigurationKey>() {
                override fun parse(raw: String, sender: CommandSender): ConfigurationKey {
                    val key = values().find { it.name.equals(raw, ignoreCase = true) }
                    if (key != null) return key
                    illegalArgument("未知配置项, 可选值: ${values().joinToString()}")
                }
            }
        }
    }

    @ValueName("service-url")
    @ValueDescription("unidbg-fetch-qsign 服务地址，可不带/结尾")
    val serviceUrl by value("http://127.0.0.1:8080")

    @ValueDescription("账户配置")
    val accounts: MutableList<Account> by value(
        mutableListOf(
            Account(
                account = "123456",
                configuration = mapOf(
                    Account.ConfigurationKey.androidId to "xxxxxxxxxxxxxxxx",
                )
            )
        )
    )

    fun getServiceUrl(path: String, arguments: Map<String, Any>): String {
        return getServiceUrl(path + arguments.map { it.key + "=" + URLEncoder.encode(it.value.toString(), "UTF-8") }.joinToString("&", "?"))
    }

    fun getServiceUrl(path: String): String {
        return serviceUrl.removeSuffix("/") + "/" + path.removePrefix("/");
    }

    fun getAccount(qq: Long): Account? {
        return accounts.firstOrNull { it.account == qq.toString() }
    }
}