package com.wanchalerm.tua.common.exception

import com.wanchalerm.tua.common.constant.ResponseEnum.CONFLICT


class DuplicateException(
    val code: String = CONFLICT.code,
    override val message: String = CONFLICT.message,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)
