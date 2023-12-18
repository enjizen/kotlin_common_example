package com.wanchalerm.tua.common.constant

enum class ResponseEnum(val code: String, val message: String) {
    SUCCESS("2000", ResponseStatusConstant.SUCCESS),
    BAD_REQUEST("4000", ResponseStatusConstant.BAD_REQUEST),
    NO_CONTENT("2004", ResponseStatusConstant.NO_CONTENT),
    CONFLICT("4009", ResponseStatusConstant.CONFLICT),
    INTERNAL_SERVER_ERROR("5000", ResponseStatusConstant.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("4001", ResponseStatusConstant.UNAUTHORIZED);

    companion object {
        fun getByCode(code: String?) =
            entries.firstOrNull { responseEnum -> responseEnum.code == code } ?: INTERNAL_SERVER_ERROR
    }
}
