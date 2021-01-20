package org.buldakov.huawei.api.client.model

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

@JsonRootName("response")
class SmsListResponse(val messages: List<Message>)