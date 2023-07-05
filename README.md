# KawaiiMiku
mirai 框架加密算法提供器。

[![mirai-core 2.15.0-dev-109+](https://img.shields.io/badge/mirai--core-2.15.0--dev--109%2B-yellowgreen)](https://github.com/mamoe/mirai)
[![Releases](https://img.shields.io/github/downloads/MrXiaoM/KawaiiMiku/total?label=%E4%B8%8B%E8%BD%BD%E9%87%8F&logo=github)](https://github.com/MrXiaoM/KawaiiMiku/releases)
[![Stars](https://img.shields.io/github/stars/MrXiaoM/KawaiiMiku?label=%E6%A0%87%E6%98%9F&logo=github)](https://github.com/MrXiaoM/KawaiiMiku/stargazers)

# 用法

只要你严格按照操作流程，就可以搭建并使用签名服务。  
所有组件均开源，由你自己操作安装可**避免被插入后门代码**。

## 1.下载开发版本

使用 [Lapis](https://mirai.mamoe.net/topic/2333) 根据操作提示，安装一个大于或等于 `2.15.0-dev-109` 的开发版本到一个新的文件夹。  
并安装本插件，启动 mirai 以生成插件配置文件，然后关闭 mirai。

此时大致目录结构如下
```
|* mirai
  |- bots
  |- config (插件配置文件夹)
    |- top.mrxiaom.mirai.kawaii
    | |- config.yml ( [本插件配置文件] )
  |- data
  |- plugins (插件文件夹)
  |- Start.cmd (mirai启动脚本，下同)
  |- Start.Server.cmd
  |- Start.sh
  |- Start.Server.sh
```

## 2.部署签名服务

首先下载服务本体，下载 [unidbg-fetch-qsign](https://github.com/fuqiuluo/unidbg-fetch-qsign/releases)，本插件适配 1.1.3 版本的签名服务。

下载后找个你记得的地方解压，这个地方记为 unidbg-fetch-qsign 目录。

本教程解压到 `mirai/unidbg-fetch-qsign-1.1.3` 文件夹下方便以后开启签名服务。

* **下载启动脚本**: 然后从本插件 [Release](https://github.com/MrXiaoM/KawaiiMiku/releases) 下载 `scripts.zip` (签名服务启动脚本)，解压到 unidbg-fetch-qsign 目录。
* **下载算法库**(可选): 到仓库 [MrXiaoM/unidbg-fetch-qsign](https://github.com/MrXiaoM/unidbg-fetch-qsign) 点击 `Code` -> `Download Zip` 拉取源代码，复制里面的 `txlib` 文件夹到 unidbg-fetch-qsign 目录中。

> 下载算法库是可选的，你下载的包已经自带算法库了。  
> 但是自带的算法库没有 8.9.58 支持，如有需要可按照以上步骤自行下载算法库。

进行以上操作后，请**确保**目录结构大致如下，
```
|* mirai
  |- bots
  |- config (插件配置文件夹)
    |- top.mrxiaom.mirai.kawaii
    | |- config.yml ( [本插件配置文件] )
  |- data
  |- plugins (插件文件夹)
  |- unidbg-fetch-qsign-1.1.3 (签名服务主目录)
    |- bin (签名服务主脚本)
    |- lib (签名服务依赖)
    |- txlib (腾讯加密算法库以及签名配置)
    | |- 8.9.58
    | | |- config.json ( [签名服务配置] )
    | | |- dtconfig.json 
    | | |- libfekit.so
    | | |- libQSec.so
    | |- 8.9.63 (这些是不同QQ版本的不同算法库和配置，目录结构同上)
    | |- 8.9.68
    |- Sign.cmd (签名服务启动脚本)
    |- Sign.sh
  |- Start.cmd (mirai启动脚本，下同)
  |- Start.Server.cmd
  |- Start.sh
  |- Start.Server.sh
```

首先选择你要启动的签名服务版本，这里以 `8.9.58` 为例。

打开 8.9.58 的**签名服务配置文件** `txlib/8.9.58/config.json`，记下 `port` 和 `key` 的数值。

**`port` 是 `端口`，`key` 是 `密钥`**。

然后打开 **签名服务启动脚本** `Sign.cmd` (Linux 是 `Sign.sh`)，输入版本号 `8.9.58` 回车启动。

> 通常不会发生端口冲突的情况，如果出现 `already in use` 之类的提示，则需要在该配置文件把 `port` 改为 `1-65535` 之间的随便一个数再启动，直到不冲突为止。

出现 `ktor.application - Responding at XXXXX:XXXX` 的提示就算启动完成了，到浏览器进入网址 `http://127.0.0.1:端口`，如果显示出 `云天明` 之类的文字则代表签名服务工作正常。

如果要关闭签名服务，在签名服务窗口按下 `Ctrl+C` 即可正常关闭。

## 3.配置签名插件

打开 **签名插件配置文件** `config/top.mrxiaom.mirai.kawaii/config.yml`。
* 将 `service-url` 改为你上一步刚刚访问的网址，即 `http://127.0.0.1:端口`
* 将 `service-key` 改为你上一步获得的 `key` 即 `密钥`

保存配置文件。

## 4.使用正确的协议版本

安装插件 [fix-protocol-version 1.8.3](https://github.com/cssxsh/fix-protocol-version/releases/tag/v1.8.3)，根据你需要的版本下载不同的设备信息文件，放到你的 mirai 目录下
* `8.9.58`: https://github.com/RomiChan/protocol-versions/blob/daae3c8f35d27f870f35ea89116914fdff7c049e/android_phone.json
* `8.9.63`: https://github.com/RomiChan/protocol-versions/blob/b74c0ac74264207d1070c651846079256d00a574/android_phone.json

然后启动 mirai。

启动 mirai 时可无视”服务注册失败“的提示。

查看启动时的提示，检查是否成功更换正确协议版本的协议信息。

若正确，可尝试登录。请保持签名服务的开启。

# 捐助

前往 [爱发电](https://afdian.net/a/mrxiaom) 捐助我。
