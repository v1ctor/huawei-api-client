package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName("request")
data class GetSmsRequest(
    @JacksonXmlProperty(localName = "PageIndex") val page: Int,
    @JacksonXmlProperty(localName = "ReadCount") val amount: Int,
    @JacksonXmlProperty(localName = "BoxType") val folder: Int,
    @JacksonXmlProperty(localName = "SortType") val sort: Int,
    @JacksonXmlProperty(localName = "Ascending") val sortDirection: Int,
    @JacksonXmlProperty(localName = "UnreadPreferred") val readFilter: Int
)