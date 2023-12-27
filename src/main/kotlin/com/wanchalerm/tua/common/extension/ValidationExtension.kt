package com.wanchalerm.tua.common.extension

import com.wanchalerm.tua.common.constant.RegexpConstant
import com.wanchalerm.tua.common.constant.RegexpConstant.EMAIL_FORMAT
import java.util.regex.Pattern

fun String?.isValidEmail(): Boolean = Pattern.matches(EMAIL_FORMAT, this)

fun String?.isValidMobileNumber(): Boolean {
    if (this?.isBlank() == true || this?.length!! > 10) return false
    return Pattern.matches(RegexpConstant.NUMBER_FORMAT_ONLY, this)
}

