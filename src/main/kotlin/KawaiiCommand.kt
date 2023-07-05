package top.mrxiaom.mirai.kawaii

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.buildCommandArgumentContext
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.console.plugin.jvm.savePluginConfig
import top.mrxiaom.mirai.kawaii.KawaiiCommand.reload
import top.mrxiaom.mirai.kawaii.KawaiiMiku.save

object KawaiiCommand : CompositeCommand(
    KawaiiMiku, "kawaii",
    description = "自动登录设置(二维码登录)",
    overrideContext = buildCommandArgumentContext {
        ServiceConfig.Account.ConfigurationKey::class with ServiceConfig.Account.ConfigurationKey.Parser
    }
) {
    @Description("添加或修改账号配置")
    @SubCommand
    suspend fun CommandSender.set(qq: Long, config: ServiceConfig.Account.ConfigurationKey? = null, value: String? = null) {
        var account = ServiceConfig.accounts.firstOrNull { it.account == qq.toString() }
        var has = true
        if (account == null) {
            has = false
            account = ServiceConfig.Account(qq.toString())
            ServiceConfig.accounts.add(account)
        }
        if (config != null && value != null) {
            account.configuration[config] = value
            KawaiiMiku.savePluginConfig(ServiceConfig)
            sendMessage("[KawaiiMiku] 已设置 $qq 的配置 $config 为 $value")
        } else if (!has) {
            KawaiiMiku.savePluginConfig(ServiceConfig)
            sendMessage("[KawaiiMiku] 已创建配置 $qq")
        } else {
            sendMessage("[KawaiiMiku] 用法: /kawaii set <qq> [config] [value]")
        }
    }
    @Description("查看账号配置")
    @SubCommand
    suspend fun CommandSender.check(qq: Long) {
        val account = ServiceConfig.accounts.firstOrNull { it.account == qq.toString() }
        if (account == null) {
            sendMessage("[KawaiiMiku] 无法找到账户 $qq 的配置")
        } else {
            val config = account.configuration
                .map { "[KawaiiMiku] ${it.key} = ${it.value}"}
                .joinToString("\n")
            sendMessage("[KawaiiMiku] $qq 的配置:\n$config")
        }
    }
    @Description("移除账号配置")
    @SubCommand
    suspend fun CommandSender.remove(qq: Long) {
        val removed = ServiceConfig.accounts.removeIf { it.account == qq.toString() }
        if (removed) {
            KawaiiMiku.savePluginConfig(ServiceConfig)
            sendMessage("[KawaiiMiku] 已移除账户 $qq 的配置")
        } else {
            sendMessage("[KawaiiMiku] 无法找到账户 $qq 的配置")
        }
    }

    @Description("从本地文件重新加载配置")
    @SubCommand
    suspend fun CommandSender.reload() {
        KawaiiMiku.reloadPluginConfig(ServiceConfig)
        sendMessage("[KawaiiMiku] 配置文件已重载")
    }
}