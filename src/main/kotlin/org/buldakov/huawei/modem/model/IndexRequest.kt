package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("request")
class IndexRequest(
    @JacksonXmlProperty(localName = "Index") val index: Int
)
