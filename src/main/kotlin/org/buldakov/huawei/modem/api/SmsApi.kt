package org.buldakov.huawei.modem.api

import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.modem.client.ModemClient
import org.buldakov.huawei.modem.model.DeleteSmsRequest
import org.buldakov.huawei.modem.model.GetSmsCountResponse
import org.buldakov.huawei.modem.model.Message
import org.buldakov.huawei.modem.model.OKResponse
import org.buldakov.huawei.modem.model.SendSmsRequest
import org.buldakov.huawei.modem.model.SmsListResponse
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.LoggerFactory

class SmsApi(private val modemClient: ModemClient) {

    private val log = LoggerFactory.getLogger(SmsApi::class.java.name)

    fun getSms(page: Int = 1, amount: Int = 1, inbox: Boolean = true, unread: Boolean = false): List<Message> {
        val data =
            """
            <request>
                <PageIndex>$page</PageIndex>
                <ReadCount>$amount</ReadCount>
                <BoxType>${if (inbox) '1' else '2'}</BoxType>
                <SortType>0</SortType>
                <Ascending>0</Ascending>
                <UnreadPreferred>${if (unread) '1' else '0'}</UnreadPreferred>
            </request>
        """
        val body = data.toRequestBody()

        val response = modemClient.makePost("/api/sms/sms-list", body, SmsListResponse::class.java)

        return response?.messages ?: emptyList()
    }

    fun sendSms(phone: String, message: String): Boolean {
        val now = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(LocalDateTime.now())
        val request = SendSmsRequest(phones = listOf(phone), content = message, length = message.length, date = now)
        val response = modemClient.makePost("/api/sms/send-sms", request, OKResponse::class.java)
        return response?.value == "OK"
    }

    fun smsCount(): GetSmsCountResponse? {
        return modemClient.makeGet("/api/sms/sms-count", GetSmsCountResponse::class.java)
    }

    fun deleteSms(indexes: List<Int>): Boolean {
        val request = DeleteSmsRequest(indexes)

        val response = modemClient.makePost("/api/sms/delete-sms", request, OKResponse::class.java)
        log.info(response.toString())

        return response?.value == "OK"
    }
}
