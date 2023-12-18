package com.wanchalerm.tua.common.exception

import org.springframework.http.HttpStatus

class BusinessException(
    var code: String,
    override var message: String,
    var httpStatus: HttpStatus = HttpStatus.CONFLICT,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)
