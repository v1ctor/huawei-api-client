package org.buldakov.huawei.api.client

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("please enter password")
    }
    val password = args[0]
    val modemApi = ModemApi("http://192.168.8.1")

    //val token = userApi.getSessionToken()

    val sessionInfo = modemApi.login("admin", password)
    while (true) {
        modemApi.getSms(sessionInfo)
        Thread.sleep(1000)
    }
}