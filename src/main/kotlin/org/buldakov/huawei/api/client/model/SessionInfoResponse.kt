package org.buldakov.huawei.api.client.model

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("response")
class SessionInfoResponse(val sesInfo: String, val tokInfo: String)