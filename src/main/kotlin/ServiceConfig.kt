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
    val serviceCmdWhiteList by value(SignClient.defaultCmdWhiteList)

    @ValueDescription("是否使用旧版本 (1.1.0 或之前) 的签名服务")
    val legacy by value(false)
    @ValueDescription("旧版本签名所需参数 QUA。可从新版签名服务的配置文件 config.json 中获得")
    val legacyQUA by value("V1_AND_SQ_8.9.58_4106_YYB_D")
}
