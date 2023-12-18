package com.wanchalerm.tua.common.extension

import java.util.Locale
import java.util.UUID

fun String?.camelToSnake() = this?.replace("([a-z])([A-Z]+)".toRegex(), "$1_$2")
                                ?.lowercase(Locale.getDefault())
                                ?: ""

fun String.createCorrelationId() = "$this-${UUID.randomUUID().toString()
                                    .replace("-", "")
                                    .lowercase()}".take(32)
