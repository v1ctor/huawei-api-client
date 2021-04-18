package org.buldakov.huawei.modem.client

import org.buldakov.huawei.modem.api.SmsApi
import org.buldakov.huawei.modem.api.UssdApi
import org.buldakov.huawei.modem.model.ReadFilter
import org.buldakov.huawei.modem.model.SmsFolder
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
        smsApi.sendSms(phone, "New message")

        var result = smsApi.getSms(folder = SmsFolder.OUTBOX)
        log.info(result.toString())

        smsApi.deleteSms(result.map { it.index })

        result = smsApi.getSms(folder = SmsFolder.OUTBOX)
        log.info(result.toString())
    }
    val ussdApi = UssdApi(modemClient)
    ussdApi.sendUssd("*123#")
    while (true) {
        val sms = smsApi.getSms(folder = SmsFolder.OUTBOX, amount = 20)
        for (s in sms) {
            log.info("$s")
            smsApi.deleteSms(s.index)
        }
        if (sms.isEmpty()) {
            break
        }
    }

    while (true) {
        val count = smsApi.smsCount()
        log.info(count.toString())
        if (count?.localInbox ?: 0 > 0) {
            log.info(smsApi.getSms(readFilter = ReadFilter.UNREAD_FIRST).toString())
        }
        Thread.sleep(1000)
    }
}
