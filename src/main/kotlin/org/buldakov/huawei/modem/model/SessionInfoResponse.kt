package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("response")
class SessionInfoResponse(val sesInfo: String, val tokInfo: String)
