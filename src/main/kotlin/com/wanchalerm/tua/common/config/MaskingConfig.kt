package com.wanchalerm.tua.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("masking")
class MaskingConfig (
    var enabled: Boolean = false,
    var maskingKeys: MutableList<String> = mutableListOf(),
    var maskingSize: Int? = 4
)