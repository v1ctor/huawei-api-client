package org.buldakov.huawei.api.client.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

data class Message(
    val index: Int,
    val phone: String,
    val content: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") val date: LocalDateTime
) {
}