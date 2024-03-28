package com.wanchalerm.tua.common.extension

import com.wanchalerm.tua.common.constant.RegexpConstant
import java.util.Locale
import java.util.UUID

fun String?.camelToSnake() = this?.replace(RegexpConstant.CAMEL_TO_SNAKE.toRegex(), "$1_$2")
                                ?.lowercase(Locale.getDefault())
                                ?: ""

fun String.createCorrelationId() = "$this-${UUID.randomUUID().toString()
                                    .replace("-", "")
                                    .lowercase()}".take(32)
