package org.buldakov.huawei.api.client

fun main() {
    val modemApi = ModemApi("http://192.168.8.1")

    //val token = userApi.getSessionToken()

    val sessionInfo = modemApi.login("admin", "")
    while (true) {
        modemApi.getSms(sessionInfo)
        Thread.sleep(1000)
    }
}