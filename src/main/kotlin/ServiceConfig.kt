package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object ServiceConfig : ReadOnlyPluginConfig("config") {
    @ValueName("service-url")
    @ValueDescription("[重启生效] unidbg-fetch-qsign 服务地址，可不带/结尾")
    val serviceUrl by value("http://127.0.0.1:8080")

    @ValueName("service-key")
    @ValueDescription("[重启生效] unidbg-fetch-qsign 服务密钥，在签名服务的 config.json 内")
    val serviceKey by value("114514")

    @ValueName("service-cmd-whitelist")
    @ValueDescription("sign 签名白名单，不懂请勿修改")
    val serviceCmdWhiteList by value(listOf<String>())
}
