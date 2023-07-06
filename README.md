# KawaiiMiku

https://github.com/MrXiaoM/KawaiiMiku

**测试未通过，目前测试结果如下，敬请期待**
> Received packet returnCode = -10005, which may mean session expired.

mirai 框架加密算法提供器。使用 [unidbg-fetch-qsign](https://github.com/fuqiuluo/unidbg-fetch-qsign) 提供的数据包签名服务。

[![mirai-core 2.15.0-core-pkgsso-19+](https://img.shields.io/badge/mirai--core-2.15.0--core--pkgsso--19-yellowgreen)](https://github.com/mamoe/mirai)
[![Releases](https://img.shields.io/github/downloads/MrXiaoM/KawaiiMiku/total?label=%E4%B8%8B%E8%BD%BD%E9%87%8F&logo=github)](https://github.com/MrXiaoM/KawaiiMiku/releases)
[![Stars](https://img.shields.io/github/stars/MrXiaoM/KawaiiMiku?label=%E6%A0%87%E6%98%9F&logo=github)](https://github.com/MrXiaoM/KawaiiMiku/stargazers)

**警告:** 使用签名服务有被冻结/封号的风险，请视自身情况决定是否使用。  
如果你的账号可以正常登录，请**不要**来折腾签名服务。

# 用法

本插件对接 unidbg-fetch-qsign 1.1.3 **测试`不通过`**，  
如果你需要，**可以[查看这篇文档](https://github.com/MrXiaoM/KawaiiMiku/blob/main/docs/LegacySetup.md)搭建已测试通过的旧版本 (1.1.0) 签名服务。**

只要你严格按照操作流程，就可以搭建并使用签名服务。  
所有组件均开源，由你自己操作安装可**避免被插入后门代码**。

**注意:** 签名服务对电脑性能有一定要求，请使用良好的 CPU 并保持内存充足。

## 1.下载开发版本

使用 [Lapis](https://mirai.mamoe.net/topic/2333) 根据操作提示，安装一个大于或等于 `2.15.0-dev-105` 的开发版本到一个新的文件夹。  
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

下载插件 [fix-protocol-version 1.8.3](https://github.com/cssxsh/fix-protocol-version/releases/tag/v1.8.3)，使用压缩软件打开 fix-protocol-version 插件 `jar`。  
旧版本的 fix-protocol-version 注册的服务在新版 mirai 是无效的，不需要管它注册的服务。

(可选) 用 [Aoki](https://github.com/MrXiaoM/Aoki) 生成自己手机的设备信息，并复制到你的包里替换掉原来的。

然后根据**你需要的版本**下载设备信息文件，放到你的 mirai 目录下。只要文件名正确，启动 mirai 时，fix-protocol-version 应当会自动加载。
* `8.9.58`: https://github.com/RomiChan/protocol-versions/blob/daae3c8f35d27f870f35ea89116914fdff7c049e/android_phone.json
* `8.9.63`: https://github.com/RomiChan/protocol-versions/blob/b74c0ac74264207d1070c651846079256d00a574/android_phone.json

然后启动 mirai。

查看启动时的提示，检查是否成功更换正确协议版本的协议信息。

若正确，可尝试登录。请保持签名服务的开启。

**至此，你已经完成了签名服务与签名插件配置。**

# 从整合包迁移

如果你想将你的插件和数据从**你正在使用的 mirai**、**某某整合包**或者**MCL**迁移到新的包，请按照以下步骤操作
* 确保新包旧包的 mirai 均已彻底关闭。
* 复制旧包的 `config`、`data`、`plugins` 文件夹到新包，已有文件全部覆盖。
* 通常只需要复制以上三个文件夹就足够了，很少有插件不把数据/配置存在 `data` 或 `config` 文件夹。
* 在新包的 `plugins` 文件夹检查有没有名字重复、版本不同的插件，删除旧版本的插件。
* 删除 `mcl-addon` 插件。(如果有的话)
* 后缀为 `.sha1` 的文件用于 MCL 校验插件文件完整性，如果你不需要可以删除。

# 如何在 mirai-core 中使用

引用本插件为依赖

```kotlin
// Gradle
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.MrXiaoM:KawaiiMiku:0.1.5")
}
```
```pom
<!-- Maven -->
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.MrXiaoM</groupId>
  <artifactId>KawaiiMiku</artifactId>
  <version>0.1.5</version>
</dependency>
```
在登录前调用

```kotlin
// kotlin
EncryptProvider.Factory.also {
    // 以后需要修改地址时使用 put(url, key)
    it.put("url", "key")
    // 此处填写 cmd whitelist
    it.cmdWhiteList = SignClient.defaultCmdWhiteList
    // 只需要注册一次
    it.registerAsOverride()
}
```
```java
// java
EncryptProvider.Factory factory = EncryptProvider.Factory.INSTANCE;
// 以后需要修改地址时使用 put(url, key)
factory.put("url", "key");
// 此处填写 cmd whitelist
factory.setCmdWhiteList(SignClient.Companion.getDefaultCmdWhiteList());
// 只需要注册一次
factory.registerAsOverride();
```
即可注册加密算法服务 以对接签名服务。

**请勿重复注册服务！如需更改地址或key，请重启程序。**

# 调试

mirai-console 下编辑 `/config/Console/Logger.yml`，在 `loggers:` 下面添加 `EncryptProvider: VERBOSE`，改好后如下
```yaml
# 特定日志记录器输出等级
loggers:
  EncryptProvider: VERBOSE
```
即可在访问签名服务器时在日志输出链接与返回结果。

# 捐助

前往 [爱发电](https://afdian.net/a/mrxiaom) 捐助我。
