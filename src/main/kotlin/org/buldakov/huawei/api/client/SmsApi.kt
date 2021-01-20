package org.buldakov.huawei.api.client

import okhttp3.RequestBody.Companion.toRequestBody
import org.buldakov.huawei.api.client.model.Message
import org.buldakov.huawei.api.client.model.SmsListResponse
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

}