package com.wanchalerm.tua.common.filter

import com.wanchalerm.tua.common.constant.ThreadConstant
import com.wanchalerm.tua.common.extension.createCorrelationId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CorrelationIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var correlationId = request.getHeader(ThreadConstant.CORRELATION_ID)
        if (correlationId?.isBlank() == true) {
            correlationId = "customer".createCorrelationId()
        }

        MDC.put(ThreadConstant.CORRELATION_ID, correlationId)
        MDC.put(ThreadConstant.CLIENT_IP, request.remoteAddr)

        filterChain.doFilter(request, response)
    }
}