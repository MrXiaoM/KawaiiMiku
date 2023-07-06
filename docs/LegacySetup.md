# 旧版模式

由于在 unidbg-fetch-qsign 1.1.3 目前测试结果一直为“-10005 会话过期”，故添加旧版模式。
unidbg-fetch-qsign 从 1.1.1 引入新机制，故小于或等于 1.1.0 的版本本文称为“旧版”。
此方法已在 unidbg-fetch-qsign 1.0.5 和 1.1.0 测试通过。

操作方法与新版大同小异。

## 1.下载开发版本


使用 [Lapis](https://mirai.mamoe.net/topic/2333) 根据操作提示，安装一个大于或等于 `2.15.0-dev-105` 的开发版本到一个新的文件夹。  

安装完成后先不安装本插件，启动 mirai，执行一次登录命令让 mirai 生成设备信息文件，关闭 mirai。  
然后用文本编辑器打开设备信息文件 `mirai/bots/QQ号/device.json`
记下文件内容最后面的 `androidId` 的值，**之后要用**，这个值的格式是 **16位长度的字母+数字**，如果不是这个格式，请确保没有按照 `mirai-device-generator` 插件，并删除 `device.json` 再登录。

然后再安装本插件，启动 mirai，再关闭 mirai，让它生成配置文件。

此时大致目录结构如下
```
|* mirai
  |- bots
    |- 114514 (机器人配置文件夹，以QQ号命名)
    | |- device.json ( [设备信息文件] )
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

首先下载服务本体，下载 [unidbg-fetch-qsign](https://github.com/fuqiuluo/unidbg-fetch-qsign/releases)，本插件**旧版模式**适配 `1.1.0` 版本的签名服务。

下载后找个你记得的地方解压，这个地方记为 unidbg-fetch-qsign 目录。

本教程解压到 `mirai/unidbg-fetch-qsign-1.1.0` 文件夹下方便以后开启签名服务。

`注意，此处下载的内容与新版教程不一致，请认真查看`

* **下载启动脚本**: 从本插件 [Release](https://github.com/MrXiaoM/KawaiiMiku/releases) 下载 `scripts.legacy.zip` (旧版签名服务启动脚本)，解压到 unidbg-fetch-qsign 目录。
* **下载算法库**: 到仓库 [fuqiuluo/unidbg-fetch-qsign (1.1.0分支)](https://github.com/fuqiuluo/unidbg-fetch-qsign/tree/1.1.0) 点击 `Code` -> `Download Zip` 拉取源代码，复制里面的 `txlib` 文件夹到 unidbg-fetch-qsign 目录中。

> 旧版的包不自带算法库，需要手动下载安装。

进行以上操作后，请**确保**目录结构大致如下，
```
|* mirai
  |- bots
    |- 114514 (机器人配置文件夹，以QQ号命名)
    | |- device.json ( [设备信息文件] )
  |- config (插件配置文件夹)
    |- top.mrxiaom.mirai.kawaii
    | |- config.yml ( [本插件配置文件] )
  |- data
  |- plugins (插件文件夹)
  |- unidbg-fetch-qsign-1.1.0 (签名服务主目录)
    |- bin (签名服务主脚本)
    |- lib (签名服务依赖)
    |- txlib (腾讯加密算法库)
    | |- 8.9.58
    | | |- libfekit.so
    | | |- libQSec.so
    | |- 8.9.63 (这些是不同QQ版本的不同算法库和配置，目录结构同上)
    | |- 8.9.68
    |- Sign.Legacy.cmd (旧版签名服务启动脚本)
    |- Sign.Legacy.sh
  |- Start.cmd (mirai启动脚本，下同)
  |- Start.Server.cmd
  |- Start.sh
  |- Start.Server.sh
```

使用文本编辑器打开签名服务的启动脚本，
在 `android_id=` 的后面填写你第一步获得的 `androidId` 然后保存，

**注意:** 每次更改设备信息文件之后，都要重新改一次启动脚本里的 `android_id`。

然后打开 **签名服务启动脚本** `Sign.Legacy.cmd` (Linux 是 `Sign.Legacy.sh`)，确认 `Android ID` 是否正确，输入版本号 `8.9.58` 回车启动。

> 通常不会发生端口冲突的情况，如果出现 `already in use` 之类的提示，则需要在该配置文件把 `port` 改为 `1-65535` 之间的随便一个数再启动，直到不冲突为止。

出现 `ktor.application - Responding at XXXXX:XXXX` 的提示就算启动完成了 (这个提示在旧版很快就被其它消息刷掉了，你瞄到有这么一句话闪过就行了)，到浏览器进入网址 `http://127.0.0.1:端口`，如果显示出 `IAA` 之类的文字则代表签名服务工作正常。

如果要关闭签名服务，在签名服务窗口按下 `Ctrl+C` 即可正常关闭。

## 3.配置签名插件

打开 **签名插件配置文件** `config/top.mrxiaom.mirai.kawaii/config.yml`。
* 将 `service-url` 改为你上一步刚刚访问的网址，即 `http://127.0.0.1:端口`
* 将 `legacyQUA` 改为你使用的协议版本的QUA。**如果你不懂，请不要修改，并确保你在使用 `8.9.58` 版本的`协议`和`签名服务`**

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

**至此，你已经完成了旧版签名服务与签名插件配置。**

# 如何在 mirai-core 中使用

在登录前调用 (`qua` 在前面有提到，为方便快速使用，此处给出 `8.9.58` 对应的 `qua` 是 `V1_AND_SQ_8.9.58_4106_YYB_D`，不同版本的 `qua` 不同，请勿在不同版本混用)

```kotlin
// kotlin
EncryptProviderLegacy.Factory.also {
    // 以后需要修改地址时使用 put(url, key, qua)
    it.put("url", "key", "V1_AND_SQ_8.9.58_4106_YYB_D")
    // 此处填写 cmd whitelist
    it.cmdWhiteList = SignClient.defaultCmdWhiteList
    // 只需要注册一次
    it.registerAsOverride()
}
```
```java
// java
EncryptProviderLegacy.Factory factory = EncryptProviderLegacy.Factory.INSTANCE;
// 以后需要修改地址时使用 put(url, key, qua)
factory.put("url", "key", "V1_AND_SQ_8.9.58_4106_YYB_D");
// 此处填写 cmd whitelist
factory.setCmdWhiteList(SignClient.Companion.getDefaultCmdWhiteList());
// 只需要注册一次
factory.registerAsOverride();
```
即可注册加密算法服务 以对接签名服务。

**请勿重复注册服务！如需更改地址或key，请重启程序。**
