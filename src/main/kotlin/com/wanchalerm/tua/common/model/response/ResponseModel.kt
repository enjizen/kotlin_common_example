package com.wanchalerm.tua.common.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.wanchalerm.tua.common.constant.ResponseEnum

@JsonPropertyOrder(
    "status",
    "data"
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ResponseModel(
    responseEnum: ResponseEnum? = null,
    responseStatus: ResponseStatus? = null,
    dataObj: Any? = null
) : ResponseCommon() {

    @JsonProperty("data")
    var dataObj: Any? = null

    init {
        this.status = responseStatus
            ?: ResponseStatus(
                responseEnum?.code,
                responseEnum?.message
            )
        this.dataObj = dataObj
    }
}