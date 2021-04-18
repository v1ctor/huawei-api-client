package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("request")
data class UssdRequest(
    @JacksonXmlProperty(localName = "content") val content: String,
    @JacksonXmlProperty(localName = "codeType") val codeType: String = "CodeType",
    @JacksonXmlProperty(localName = "timeout") val timeout: String = ""
)