package org.buldakov.huawei.api.client

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.api.client.model.SessionInfo
import org.buldakov.huawei.api.client.utils.base64
import org.buldakov.huawei.api.client.utils.sha256
import java.util.logging.Logger

class ModemApi(private val baseUrl: String) {

    private val log = Logger.getLogger(ModemApi::class.java.name)

    private fun getSessionToken(): SessionInfo {
        val url = "$baseUrl/api/webserver/SesTokInfo"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()

        val body = response.body?.string()

        var sessionId = response.headers.get("SessionID")
        log.info(body)
        val tree = XmlMapper().readTree(body)
        if (sessionId == null) {
            sessionId = tree.get("SesInfo") .asText()
        }
        val tokInfo = tree.get("TokInfo")

        val token = tokInfo.asText()
        log.info("Get token $token")
        return SessionInfo(sessionId!!, token)
    }

    fun login(username: String, password: String): SessionInfo {
        val sessionInfo = getSessionToken()
        val authToken = authToken(username, password, sessionInfo.loginToken)
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
            .header("__RequestVerificationToken", sessionInfo.loginToken)
            .header("Cookie", "SessionID=${sessionInfo.sessionId}")
            .header("X-Requested-With", "XMLHttpRequest")
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        log.info(response.body?.string())
        log.info(response.header("__RequestVerificationToken"))
        log.info(response.header("SessionID"))

        val tokenHeader = response.header("__RequestVerificationToken")


        return SessionInfo(sessionInfo.sessionId, tokenHeader!!)
    }


    fun getSms(sessionInfo: SessionInfo) {
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
            .header("__RequestVerificationToken", sessionInfo.loginToken)
            .header("Cookie", "SessionID=${sessionInfo.sessionId}")
            .header("X-Requested-With", "XMLHttpRequest")
            .build()
        val response = client.newCall(request).execute()

        log.info(response.body?.string())
        log.info(response.headers.toString())
    }


    private fun authToken(username: String, password: String, loginToken: String): String {
        return (username + password.sha256().base64() + loginToken).sha256().base64()
    }
}