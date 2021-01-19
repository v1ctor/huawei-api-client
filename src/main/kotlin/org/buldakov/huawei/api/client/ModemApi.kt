package org.buldakov.huawei.api.client

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import okhttp3.Cookie
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.api.client.utils.base64
import org.buldakov.huawei.api.client.utils.sha256
import org.slf4j.LoggerFactory

class ModemApi(private val baseUrl: String) {

    private val log = LoggerFactory.getLogger(ModemApi::class.java.name)

    private var sessionId: String? = null
    private var loginToken: String? = null
    private var tokens = mutableListOf<String>()

    private fun prepareSessionInfo() {
        val url = "$baseUrl/api/webserver/SesTokInfo"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()

        val body = response.body?.string()

        processHeaders(response.headers)
        log.info(body)
        log.info(sessionId)
        val tree = XmlMapper().readTree(body)
        if (sessionId == null) {
            sessionId = tree.get("SesInfo").asText()
        }
        val tokInfo = tree.get("TokInfo")

        loginToken = tokInfo.asText()
        log.info("Get token $loginToken")
        log.info("Get session $sessionId")
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
        // TODO currently arraylist.
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
        log.info("Login token $authToken")
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

        log.info("Login body {}", response.body?.string())
        log.info("Login session id {}", sessionId)
    }

    fun getSms() {
        getSessionInfo()
        val data = """
            <request>
                <PageIndex>1</PageIndex>
                <ReadCount>1</ReadCount>
                <BoxType>1</BoxType>
                <SortType>0</SortType>
                <Ascending>0</Ascending>
                <UnreadPreferred>0</UnreadPreferred>
            </request>
        """
        val body = data.toRequestBody()
        val url = "$baseUrl/api/sms/sms-list"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).post(body)
                .header("__RequestVerificationToken", token()!!)
                .header("Cookie", "SessionID=${sessionId}")
                .header("X-Requested-With", "XMLHttpRequest")
                .build()
        val response = client.newCall(request).execute()

        log.info("Sms body {}", response.body?.string())
        log.info("sms headers {}", response.headers.toString())
    }


    private fun authToken(username: String, password: String, loginToken: String): String {
        return (username + password.sha256().base64() + loginToken).sha256().base64()
    }
}