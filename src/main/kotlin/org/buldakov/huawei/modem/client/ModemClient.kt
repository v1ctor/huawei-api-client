package org.buldakov.huawei.modem.client

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.modem.model.SessionInfoResponse
import org.buldakov.huawei.modem.utils.base64
import org.buldakov.huawei.modem.utils.sha256
import org.buldakov.huawei.modem.xml.getXmlMapper
import org.slf4j.LoggerFactory

class ModemClient(private val baseUrl: String) {

    private val log = LoggerFactory.getLogger(ModemClient::class.java.name)

    private var sessionId: String? = null
    private var loginToken: String? = null
    private var tokens = mutableListOf<String>()

    private val httpClient = OkHttpClient()
    private val xmlMapper = getXmlMapper()

    private fun prepareSessionInfo() {
        val url = "$baseUrl/api/webserver/SesTokInfo"
        val request = Request.Builder().url(url).get().build()
        val response = httpClient.newCall(request).execute()

        val body = response.body?.string()

        processHeaders(response.headers)
        val sessionInfo = xmlMapper.readValue(body, SessionInfoResponse::class.java)
        if (sessionId == null) {
            sessionId = sessionInfo.sesInfo
        }
        loginToken = sessionInfo.tokInfo
    }

    private fun getSessionInfo() {
        if (sessionId == null || loginToken == null) {
            prepareSessionInfo()
        }
    }

    private fun token(): String? {
        if (tokens.isEmpty()) {
            log.warn("No token you need to login again.")
            return null
        }
        return tokens.removeLast()
    }

    private fun processTokens(header: String) {
        val strings = header.split("#").filter { it.isNotBlank() }
        if (strings.size > 1) {
            tokens = strings.subList(2, strings.lastIndex).toMutableList()
        } else if (strings.size == 1) {
            tokens.add(strings[0])
        }
    }

    private fun processHeaders(headers: Headers) {
        val cookies = Cookie.parseAll(baseUrl.toHttpUrl(), headers)
        cookies.find { cookie -> cookie.name == "SessionID" }?.value?.let { sessionId = it }

        headers["__RequestVerificationToken"]?.let { processTokens(it) }
    }

    fun login(username: String, password: String) {
        getSessionInfo()
        val authToken = authToken(username, password, loginToken!!)
        val data = """<?xml version:"1.0" encoding="UTF-8"?>
                    <request>
                        <Username>${username}</Username>
                        <Password>$authToken</Password>
                        <password_type>4</password_type>
                    </request>
                """

        val body = data.toRequestBody()
        val request = Request.Builder().url("$baseUrl/api/user/login").post(body)
            .header("__RequestVerificationToken", loginToken!!)
            .header("Cookie", "SessionID=${sessionId}")
            .header("X-Requested-With", "XMLHttpRequest")
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        processHeaders(response.headers)
    }

    fun makePost(path: String, requestBody: RequestBody): ResponseBody? {
        getSessionInfo()

        val request = Request.Builder().url("$baseUrl$path").post(requestBody)
            .header("__RequestVerificationToken", token()!!)
            .header("Cookie", "SessionID=${sessionId}")
            .header("X-Requested-With", "XMLHttpRequest")
            .build()

        try {
            val response = httpClient.newCall(request).execute()
            processHeaders(response.headers)

            return response.body
        } catch (e: Exception) {
            log.error("Error while making a post request", e)
        }
        return null
    }

    fun <T> makePost(path: String, requestBody: RequestBody, clazz: Class<T>): T? {
        return makePost(path, requestBody)?.byteStream()?.let { xmlMapper.readValue(it, clazz) }
    }

    private fun authToken(username: String, password: String, loginToken: String): String {
        return (username + password.sha256().base64() + loginToken).sha256().base64()
    }
}