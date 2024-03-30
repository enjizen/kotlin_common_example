package com.wanchalerm.tua.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("masking")
class MaskingConfig (
    var maskingKeys: MutableList<String> = mutableListOf(),
    var maskingSize: Int? = 4,
    var hiddenKeys: MutableList<String> = mutableListOf()
)