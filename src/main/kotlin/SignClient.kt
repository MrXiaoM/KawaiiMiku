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
