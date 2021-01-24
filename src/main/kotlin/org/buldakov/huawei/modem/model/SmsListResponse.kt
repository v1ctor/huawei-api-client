package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("response")
class SmsListResponse(
    @JacksonXmlProperty(localName = "Count")
    val count: Int,
    @JacksonXmlProperty(localName = "Messages")
    @JacksonXmlElementWrapper(useWrapping = true)
    val messages: List<Message>
)
