package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("response")
class SmsListResponse(val messages: List<Message>)
