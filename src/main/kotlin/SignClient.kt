package top.mrxiaom.mirai.kawaii

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.net.URLEncoder


abstract class SignClient {
    companion object {
        val defaultCmdWhiteList: List<String> = listOf(
            "OidbSvcTrpcTcp.0x55f_0",
            "OidbSvcTrpcTcp.0x1100_1",
            "qidianservice.269",
            "OidbSvc.0x4ff_9_IMCore",
            "MsgProxy.SendMsg",
            "SQQzoneSvc.shuoshuo",
            "OidbSvc.0x758_1",
            "QChannelSvr.trpc.qchannel.commwriter.ComWriter.DoReply",
            "trpc.login.ecdh.EcdhService.SsoNTLoginPasswordLoginUnusualDevice",
            "wtlogin.device_lock",
            "OidbSvc.0x758_0",
            "wtlogin_device.tran_sim_emp",
            "OidbSvc.0x4ff_9",
            "trpc.springfestival.redpacket.LuckyBag.SsoSubmitGrade",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoReply",
            "trpc.o3.report.Report.SsoReport",
            "SQQzoneSvc.addReply",
            "OidbSvc.0x8a1_7",
            "QChannelSvr.trpc.qchannel.commwriter.ComWriter.DoComment",
            "OidbSvcTrpcTcp.0xf67_1",
            "friendlist.ModifyGroupInfoReq",
            "OidbSvcTrpcTcp.0xf65_1",
            "OidbSvcTrpcTcp.0xf65_10",
            "OidbSvcTrpcTcp.0xf65_10",
            "OidbSvcTrpcTcp.0xf67_5",
            "OidbSvc.0x56c_6",
            "OidbSvc.0x8ba",
            "SQQzoneSvc.like",
            "OidbSvcTrpcTcp.0xf88_1",
            "OidbSvc.0x8a1_0",
            "wtlogin.name2uin",
            "SQQzoneSvc.addComment",
            "wtlogin.login",
            "trpc.o3.ecdh_access.EcdhAccess.SsoSecureA2Access",
            "OidbSvcTrpcTcp.0x101e_2",
            "qidianservice.135",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoComment",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoBarrage",
            "OidbSvcTrpcTcp.0x101e_1",
            "OidbSvc.0x89a_0",
            "friendlist.addFriend",
            "ProfileService.GroupMngReq",
            "OidbSvc.oidb_0x758",
            "MessageSvc.PbSendMsg",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoLike",
            "OidbSvc.0x758",
            "trpc.o3.ecdh_access.EcdhAccess.SsoSecureA2Establish",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoPush",
            "qidianservice.290",
            "trpc.qlive.relationchain_svr.RelationchainSvr.Follow",
            "trpc.o3.ecdh_access.EcdhAccess.SsoSecureAccess",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.DoFollow",
            "SQQzoneSvc.forward",
            "ConnAuthSvr.sdk_auth_api",
            "wtlogin.qrlogin",
            "wtlogin.register",
            "OidbSvcTrpcTcp.0x6d9_4",
            "trpc.passwd.manager.PasswdManager.SetPasswd",
            "friendlist.AddFriendReq",
            "qidianservice.207",
            "ProfileService.getGroupInfoReq",
            "OidbSvcTrpcTcp.0x1107_1",
            "OidbSvcTrpcTcp.0x1105_1",
            "SQQzoneSvc.publishmood",
            "wtlogin.exchange_emp",
            "OidbSvc.0x88d_0",
            "wtlogin_device.login",
            "OidbSvcTrpcTcp.0xfa5_1",
            "trpc.qqhb.qqhb_proxy.Handler.sso_handle",
            "OidbSvcTrpcTcp.0xf89_1",
            "OidbSvc.0x9fa",
            "FeedCloudSvr.trpc.feedcloud.commwriter.ComWriter.PublishFeed",
            "QChannelSvr.trpc.qchannel.commwriter.ComWriter.PublishFeed",
            "OidbSvcTrpcTcp.0xf57_106",
            "ConnAuthSvr.sdk_auth_api_emp",
            "OidbSvcTrpcTcp.0xf6e_1",
            "trpc.qlive.word_svr.WordSvr.NewPublicChat",
            "trpc.passwd.manager.PasswdManager.VerifyPasswd",
            "trpc.group_pro.msgproxy.sendmsg",
            "OidbSvc.0x89b_1",
            "OidbSvcTrpcTcp.0xf57_9",
            "FeedCloudSvr.trpc.videocircle.circleprofile.CircleProfile.SetProfile",
            "OidbSvc.0x6d9_4",
            "OidbSvcTrpcTcp.0xf55_1",
            "ConnAuthSvr.fast_qq_login",
            "OidbSvcTrpcTcp.0xf57_1",
            "trpc.o3.ecdh_access.EcdhAccess.SsoEstablishShareKey",
            "wtlogin.trans_emp"
        )
    }

    abstract val url: String
    abstract val key: String
    fun getServiceUrl(path: String, arguments: Map<String, Any>): String {
        return getServiceUrl(path + arguments.map { it.key + "=" + URLEncoder.encode(it.value.toString(), "UTF-8") }.joinToString("&", "?"))
    }

    fun getServiceUrl(path: String): String {
        return url.removeSuffix("/") + "/" + path.removePrefix("/");
    }

    fun get(path: String): JsonElement? {
        return httpGet(getServiceUrl(path))
    }

    fun get(path: String, vararg arguments: Pair<String, Any>): JsonElement? {
        return httpGet(getServiceUrl(path, arguments.toMap()))
    }

    fun post(path: String, vararg arguments: Pair<String, Any>): JsonElement? {
        return httpPostUrlEncoded(getServiceUrl(path), arguments.toMap())
    }
}

fun httpGet(url: String): JsonElement? {
    HttpClients.createDefault().use {
        val response = it.execute(HttpGet(url).also { httpGet ->
            httpGet.config = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .setRedirectsEnabled(true)
                .build()
        })
        if (response.statusLine.statusCode == 200) {
            val result = EntityUtils.toString(response.entity)
            EncryptProvider.logger.verbose("$url :\n$result")
            val json = Json.decodeFromString(DataWrapper.serializer(), result)
            if (json.code == 0) {
                return json.data
            } else {
                EncryptProvider.logger.warning("ERROR ON $url, \ncode = ${json.code}, msg = ${json.message}")
            }
        } else {
            EncryptProvider.logger.warning("HTTP GET ERROR ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}, url = $url")
        }
        return null
    }
}

fun httpPostUrlEncoded(url: String, arguments: Map<String, Any>): JsonElement? {
    HttpClients.createDefault().use { client ->
        val args = arguments
            .map { "${it.key}=${URLEncoder.encode(it.value.toString(), "UTF-8")}" }
            .joinToString("&")
        val response = client.execute(HttpPost(url).also { httpPost ->
            httpPost.entity = StringEntity(args, ContentType.APPLICATION_FORM_URLENCODED)
        })
        if (response.statusLine.statusCode == 200) {
            val result = EntityUtils.toString(response.entity)
            EncryptProvider.logger.verbose("$url $args :\n$result")
            val json = Json.decodeFromString(DataWrapper.serializer(), result)
            if (json.code == 0) {
                return json.data
            } else {
                EncryptProvider.logger.warning("ERROR ON $url, \ncode = ${json.code}, msg = ${json.message}")
            }
        } else {
            EncryptProvider.logger.warning("HTTP POST ERROR ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}, url = $url, arguments = $args")
        }
        return null
    }
}

@Serializable
private data class DataWrapper(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("msg")
    val message: String = "",
    @SerialName("data")
    val data: JsonElement
)
