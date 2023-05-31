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
    @ValueDescription("API算法版本，即参数 name 的值")
    val apiVersion by value("8.9.50")
}