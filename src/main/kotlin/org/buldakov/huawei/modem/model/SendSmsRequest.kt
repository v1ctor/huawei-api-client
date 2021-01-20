package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("request")
class SendSmsRequest(
    @JacksonXmlProperty(localName = "Index") val index: Int = -1,
    @JacksonXmlElementWrapper(localName = "Phones")
    @JacksonXmlProperty(localName = "Phone") val phones: List<String>,
    @JacksonXmlProperty(localName = "Sca") val sca: String = "",
    @JacksonXmlProperty(localName = "Content") val content: String,
    @JacksonXmlProperty(localName = "Length") val length: Int,
    @JacksonXmlProperty(localName = "Reserved") val reserved: Int = 1,
    @JacksonXmlProperty(localName = "Date") val date: String,
)
