package com.wanchalerm.tua.common.exception

import com.wanchalerm.tua.common.constant.ResponseEnum.BAD_REQUEST


class DuplicateException(
    val code: String = BAD_REQUEST.code,
    override val message: String = BAD_REQUEST.message,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)
