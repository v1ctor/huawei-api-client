package org.buldakov.huawei.api.client

import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("huawei-api-client")

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("please enter password")
    }
    val password = args[0]
    var phone: String? = null
    if (args.size > 1) {
        phone = args[1]
    }
    val modemClient = ModemClient("http://192.168.8.1")

    modemClient.login("admin", password)
    val smsApi = SmsApi(modemClient)
    if (phone != null) {
        smsApi.sendSms(phone, "Send from API")
    }
    while (true) {
        log.info(smsApi.getSms().toString())
        Thread.sleep(1000)
    }
}