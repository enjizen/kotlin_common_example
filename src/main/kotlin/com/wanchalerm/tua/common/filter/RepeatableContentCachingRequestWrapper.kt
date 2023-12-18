package com.wanchalerm.tua.common.filter

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import java.io.IOException
import org.springframework.web.util.ContentCachingRequestWrapper

class RepeatableContentCachingRequestWrapper(request: HttpServletRequest?) : ContentCachingRequestWrapper(request!!) {
    private var inputStream: SimpleServletInputStream? = null
    override fun getInputStream(): ServletInputStream = this.inputStream!!

    @Throws(IOException::class)
    fun readInputAndDuplicate(): String {
        if (inputStream == null) {
            val body = super.getInputStream().readAllBytes()
            this.inputStream = SimpleServletInputStream(body)
        }
        return String(super.getContentAsByteArray())
    }
}