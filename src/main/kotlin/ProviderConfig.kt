package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object ProviderConfig : ReadOnlyPluginConfig("config") {
    @ValueName("api-url")
    @ValueDescription("API链接，url = \"\$apiUrl/energy?name=...\"")
    val apiUrl by value("http://api.tencentola.com/txapi")
    @ValueName("api-version")
    @ValueDescription("(算法)API算法版本，即参数 name 的值")
    val apiVersion by value("8.9.50")
    @ValueName("api-version")
    @ValueDescription("(签名)API算法版本，即参数 name 的值")
    val apiVersionSign by value("8.9.58")
    @ValueDescription("QUA，签名所需参数，可以在客户端内置浏览器的UA中获取，不知道怎么填的不要乱填")
    val qua by value(mapOf("ANDROID_PHONE" to "V1_AND_SQ_8.9.50_3898_YYB_D"))
}