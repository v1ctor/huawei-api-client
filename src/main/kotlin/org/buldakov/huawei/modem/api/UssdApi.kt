package org.buldakov.huawei.modem.api

import org.buldakov.huawei.modem.client.ModemClient
import org.buldakov.huawei.modem.model.OKResponse
import org.buldakov.huawei.modem.model.UssdRequest

class UssdApi(private val modemClient: ModemClient) {

    fun sendUssd(message: String): Boolean {
        val request = UssdRequest(content = message)
        val response = modemClient.makePost("/api/ussd/send", request, OKResponse::class.java)
        return response?.value == "OK"
    }

}