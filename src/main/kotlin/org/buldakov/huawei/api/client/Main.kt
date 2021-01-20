package org.buldakov.huawei.api.client

import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("huawei-api-client")

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("please enter password")
    }
    val password = args[0]
    val modemClient = ModemClient("http://192.168.8.1")

    modemClient.login("admin", password)
    val smsApi = SmsApi(modemClient)
    while (true) {
        log.info(smsApi.getSms().toString())
        Thread.sleep(1000)
    }
}