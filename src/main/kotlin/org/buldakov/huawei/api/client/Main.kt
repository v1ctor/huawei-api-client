package org.buldakov.huawei.api.client

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("please enter password")
    }
    val password = args[0]
    val modemClient = ModemClient("http://192.168.8.1")

    //val token = userApi.getSessionToken()

    modemClient.login("admin", password)
    val smsApi = SmsApi(modemClient)
    while (true) {
        smsApi.getSms()
        Thread.sleep(1000)
    }
}