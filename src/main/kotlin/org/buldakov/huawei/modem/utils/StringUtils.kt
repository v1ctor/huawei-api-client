package org.buldakov.huawei.modem.utils

import java.security.MessageDigest
import java.util.Base64

fun digest(algorithm: String, input: String): String {
    return MessageDigest.getInstance(algorithm).digest(input.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
}

fun String.sha256(): String {
    return digest("SHA-256", this)
}

fun String.base64(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}
