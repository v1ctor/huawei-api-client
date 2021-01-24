package org.buldakov.huawei.modem.api

import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.modem.client.ModemClient
import org.buldakov.huawei.modem.model.DeleteSmsRequest
import org.buldakov.huawei.modem.model.GetSmsCountResponse
import org.buldakov.huawei.modem.model.GetSmsRequest
import org.buldakov.huawei.modem.model.IndexRequest
import org.buldakov.huawei.modem.model.Message
import org.buldakov.huawei.modem.model.OKResponse
import org.buldakov.huawei.modem.model.ReadFilter
import org.buldakov.huawei.modem.model.SendSmsRequest
import org.buldakov.huawei.modem.model.SmsFolder
import org.buldakov.huawei.modem.model.SmsListResponse
import org.buldakov.huawei.modem.model.SortOrder
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.LoggerFactory

class SmsApi(private val modemClient: ModemClient) {

    private val log = LoggerFactory.getLogger(SmsApi::class.java.name)

    fun getSms(
        page: Int = 1,
        amount: Int = 1,
        folder: SmsFolder = SmsFolder.INBOX,
        readFilter: ReadFilter = ReadFilter.UNREAD_FIRST,
        sortOrder: SortOrder = SortOrder.DESC
    ): List<Message> {
        val request = GetSmsRequest(page, amount, folder.tag, 0, sortOrder.tag, readFilter.tag)
        val response = modemClient.makePost("/api/sms/sms-list", request, SmsListResponse::class.java)

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

    fun deleteSms(index: Int): Boolean {
        return deleteSms(listOf(index))
    }

    fun deleteSms(indexes: List<Int>): Boolean {
        val request = DeleteSmsRequest(indexes)

        val response = modemClient.makePost("/api/sms/delete-sms", request, OKResponse::class.java)
        log.info(response.toString())

        return response?.value == "OK"
    }

    fun markReadSms(index: Int): Boolean {
        val request = IndexRequest(index)
        val response = modemClient.makePost("/api/sms/set-read", request, OKResponse::class.java)

        return response?.value == "OK"
    }
}
