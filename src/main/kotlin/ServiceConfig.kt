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
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.net.URLEncoder

object ServiceConfig : ReadOnlyPluginConfig("config") {
    @Serializable
    data class Account(
        @Comment("QQ账号")
        val account: String,
        @Comment(
            """
            账号配置. 暂无可用配置
        """
        )
        val configuration: MutableMap<ConfigurationKey, @Serializable(with = YamlDynamicSerializer::class) Any> = mutableMapOf(
        ),
    ) {
        @Serializable
        enum class ConfigurationKey {
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
                configuration = mutableMapOf()
            )
        )
    )

    fun getServiceUrl(path: String, arguments: Map<String, Any>): String {
        return getServiceUrl(path + arguments.map { it.key + "=" + URLEncoder.encode(it.value.toString(), "UTF-8") }.joinToString("&", "?"))
    }

    fun getServiceUrl(path: String): String {
        return serviceUrl.removeSuffix("/") + "/" + path.removePrefix("/");
    }

    fun get(path: String): String? {
        return httpGet(getServiceUrl(path))
    }

    fun get(path: String, arguments: Map<String, Any>): String? {
        return httpGet(getServiceUrl(path, arguments))
    }

    fun post(path: String, arguments: Map<String, Any>): String? {
        return httpPostUrlEncoded(getServiceUrl(path), arguments)
    }

    fun getAccount(qq: Long): Account? {
        return accounts.firstOrNull { it.account == qq.toString() }
    }
}

fun httpGet(url: String): String? {
    HttpClients.createDefault().use {
        val response = it.execute(HttpGet(url).also { httpGet ->
            httpGet.config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setRedirectsEnabled(true)
                .build()
        })
        if (response.statusLine.statusCode == 200) {
            return EntityUtils.toString(response.entity)
        }
        return null
    }
}

fun httpPostUrlEncoded(url: String, arguments: Map<String, Any>): String? {
    HttpClients.createDefault().use { client ->
        val response = client.execute(HttpPost(url).also { httpPost ->
            val args = arguments
                .map { "${it.key}=${URLEncoder.encode(it.value.toString(), "UTF-8")}" }
                .joinToString("&")
            httpPost.entity = StringEntity(args, ContentType.APPLICATION_FORM_URLENCODED)
        })
        if (response.statusLine.statusCode == 200) {
            return EntityUtils.toString(response.entity)
        }
        return null
    }
}
