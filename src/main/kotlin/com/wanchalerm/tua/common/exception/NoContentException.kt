package com.wanchalerm.tua.common.exception

import com.wanchalerm.tua.common.constant.ResponseEnum.NO_CONTENT


class NoContentException (
    val code: String = NO_CONTENT.code,
    override var message: String = NO_CONTENT.message,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)