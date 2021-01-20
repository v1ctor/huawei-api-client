package org.buldakov.huawei.api.client

import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.api.client.model.Message
import org.buldakov.huawei.api.client.model.SmsListResponse
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.slf4j.LoggerFactory

class SmsApi(private val modemClient: ModemClient) {

    private val log = LoggerFactory.getLogger(SmsApi::class.java.name)

    fun getSms(unread: Boolean = false): List<Message> {
        val data = """
            <request>
                <PageIndex>1</PageIndex>
                <ReadCount>1</ReadCount>
                <BoxType>1</BoxType>
                <SortType>0</SortType>
                <Ascending>0</Ascending>
                <UnreadPreferred>${if (unread) '1' else '0'}</UnreadPreferred>
            </request>
        """
        val body = data.toRequestBody()

        val response = modemClient.makePost("/api/sms/sms-list", body, SmsListResponse::class.java)

        return response?.messages ?: emptyList()
    }

    fun sendSms(phone: String, message: String) {
        val now = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(LocalDateTime.now())
        val data = """<request><Index>-1</Index><Phones><Phone>$phone</Phone></Phones><Sca></Sca><Content>$message</Content><Length>${message.length}</Length><Reserved>1</Reserved><Date>$now</Date></request>"""
        val body = data.toRequestBody()
        val response = modemClient.makePost("/api/sms/send-sms", body)
        log.info(response?.string())
    }

}