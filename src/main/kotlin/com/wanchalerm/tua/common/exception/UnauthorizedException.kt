package com.wanchalerm.tua.common.exception

import com.wanchalerm.tua.common.constant.ResponseEnum.UNAUTHORIZED


class UnauthorizedException (
    var code: String? = UNAUTHORIZED.code,
    override var message: String? = UNAUTHORIZED.message,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)