package org.buldakov.huawei.modem.model

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("response")
data class GetSmsCountResponse(
    val localUnread: Int,
    val localInbox: Int,
    val localOutbox: Int,
    val localDraft: Int,
    val localDeleted: Int,
    val simUnread: Int,
    val simInbox: Int,
    val simOutbox: Int,
    val simDraft: Int,
    val localMax: Int,
    val simMax: Int,
    val simUsed: Int,
    val newMsg: Int
)