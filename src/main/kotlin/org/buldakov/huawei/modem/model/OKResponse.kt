package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonRootName("response")
class OKResponse {

    @JacksonXmlText lateinit var value: String
}
