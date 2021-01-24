package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import org.joda.time.LocalDateTime

@JsonRootName("Message")
data class Message(
    @JacksonXmlProperty(localName = "Index") val index: Int,
    @JacksonXmlProperty(localName = "Phone") val phone: String,
    @JacksonXmlProperty(localName = "Content") val content: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JacksonXmlProperty(localName = "Date")
    val date: LocalDateTime
)
