package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("request")
data class DeleteSmsRequest(
    @JacksonXmlProperty(localName = "Index")
    @JacksonXmlElementWrapper(useWrapping = false)
    val indexes: List<Int>
) {
}