package org.buldakov.huawei.api.client.model

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("response")
class SmsListResponse(val messages: List<Message>)