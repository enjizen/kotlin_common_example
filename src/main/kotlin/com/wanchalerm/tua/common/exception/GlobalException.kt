package com.wanchalerm.tua.common.exception

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.wanchalerm.tua.common.constant.ContentType
import com.wanchalerm.tua.common.constant.ExceptionConstant.DEFAULT_MESSAGE
import com.wanchalerm.tua.common.constant.ExceptionConstant.FIELD_IS_INVALID
import com.wanchalerm.tua.common.constant.ExceptionConstant.FIELD_IS_MISSING
import com.wanchalerm.tua.common.constant.ResponseEnum
import com.wanchalerm.tua.common.extension.camelToSnake
import com.wanchalerm.tua.common.model.response.ResponseModel
import com.wanchalerm.tua.common.model.response.ResponseStatus
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.bind.*
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.TreeMap


@RestControllerAdvice
class GlobalException {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)


    @ExceptionHandler(
        JsonParseException::class,
        HttpMediaTypeException::class,
        HttpMessageNotReadableException::class,
        MissingRequestHeaderException::class,
        MissingRequestValueException::class,
        ServletRequestBindingException::class,
        MethodArgumentTypeMismatchException::class
    )
    @ResponseBody
    fun handleJsonParseException(ex: Exception): ResponseEntity<ResponseModel> {
        logger.error("handleJsonParseException", ex)
        val description = getDescription(ex)
        val responseStatus = ResponseStatus("400", description)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(ResponseModel(responseStatus = responseStatus))
    }

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        ConstraintViolationException::class
    )
    @ResponseBody
    fun handleBadRequest(ex: Exception): ResponseEntity<ResponseModel> {
        logger.error("handleBadRequest", ex)
        val errorMap = TreeMap<String, String>()

        if (ex is MethodArgumentNotValidException) {
            ex.bindingResult.fieldErrors.forEach { error ->
                val errorDescription = if (error.rejectedValue == null) FIELD_IS_MISSING else FIELD_IS_INVALID
                error.field.camelToSnake().let { errorMap[it] = String.format(errorDescription, it) }
            }

            ex.bindingResult.globalErrors.forEach { error ->
                val errorDescription = if (error.defaultMessage == "null") FIELD_IS_MISSING else FIELD_IS_INVALID
                error.objectName.camelToSnake().let { errorMap[it] = String.format(errorDescription, it) }
            }
        }

        (ex as? ConstraintViolationException)?.constraintViolations?.forEach { error ->
            val errorDescription = if (error.invalidValue == null) FIELD_IS_MISSING else FIELD_IS_INVALID
            val paths = error.propertyPath.toString().split(".")
            val fieldNameSnakeCase = paths[paths.size - 1].camelToSnake()
            errorMap[fieldNameSnakeCase] = String.format(errorDescription, fieldNameSnakeCase)
        }

        val responseStatus = ResponseStatus(
            ResponseEnum.BAD_REQUEST.code,
            errorMap.firstEntry().value ?: ResponseEnum.BAD_REQUEST.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(ResponseModel(responseStatus = responseStatus))

    }

    private fun getDescription(exception: Exception): String {
        return when {
            exception.cause is JsonMappingException && (exception.cause as JsonMappingException).cause is InputValidationException -> {
                (exception.cause as JsonMappingException).let { cause ->
                    String.format(FIELD_IS_INVALID, referencesToFields(cause.path))
                }
            }

            exception.cause is InvalidFormatException -> (exception.cause as InvalidFormatException).let { cause ->
                String.format(FIELD_IS_INVALID, referencesToFields(cause.path))
            }

            exception is MissingServletRequestParameterException -> String.format(
                FIELD_IS_MISSING,
                exception.parameterName
            )

            exception is MissingPathVariableException -> String.format(FIELD_IS_MISSING, exception.variableName)

            exception is MethodArgumentTypeMismatchException -> String.format(FIELD_IS_INVALID, exception.name)

            else -> DEFAULT_MESSAGE
        }

    }

    private fun referencesToFields(references: List<JsonMappingException.Reference>): String {
        val sb = StringBuilder()
        references.forEach { ref ->
            if (ref.fieldName == null) {
                sb.deleteCharAt(sb.length - 1)
                    .append("[")
                    .append(ref.index)
                    .append("]")
            } else {
                sb.append(ref.fieldName)
            }
            sb.append(".")
        }
        return sb.substring(0, sb.length - 1)
    }

    @ExceptionHandler(BusinessException::class)
    @ResponseBody
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ResponseModel> {
        logger.info(ex.toString())
        return ResponseEntity.status(ex.httpStatus)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(ResponseModel(responseStatus = ResponseStatus(ex.code, ex.message)))
    }

    @ExceptionHandler(InputValidationException::class)
    @ResponseBody
    fun handleInputValidationException(ex: InputValidationException): ResponseEntity<ResponseModel> {
        logger.info(ex.toString())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(ResponseModel(responseStatus = ResponseStatus(ex.code, ex.message)))
    }

    @ExceptionHandler(NoContentException::class)
    @ResponseBody
    fun handleNoContentExceptionException(ex: NoContentException): ResponseEntity<ResponseModel> {
        logger.info(ex.toString())
        val responseModel = ResponseModel(responseStatus = ResponseStatus(ex.code, ex.message))
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(responseModel)
    }

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseBody
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ResponseModel> {
        logger.info(ex.toString())
        val responseModel = ResponseModel(responseStatus = ResponseStatus(ex.code, ex.message))
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(responseModel)
    }

    @ExceptionHandler(DuplicateException::class)
    @ResponseBody
    fun handleDuplicateException(ex: DuplicateException): ResponseEntity<ResponseModel> {
        logger.info(ex.toString())
        val responseModel = ResponseModel(responseStatus = ResponseStatus(ex.code, ex.message))
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
            .body(responseModel)
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleGenericException(ex: Exception): ResponseEntity<ResponseModel> {
        logger.error(ex.message, ex)

        val responseStatus = when {
            isGatewayTimeoutException(ex) -> ResponseStatus("504", "System timeout, please try again later")
            else -> ResponseStatus("500", "Internal server error")
        }

        val responseModel = ResponseModel(responseStatus = responseStatus)

        val httpStatus = if (responseStatus.code == "504") HttpStatus.GATEWAY_TIMEOUT else HttpStatus.INTERNAL_SERVER_ERROR

        return ResponseEntity
            .status(httpStatus)
            .body(responseModel)
    }

    private fun isGatewayTimeoutException(ex: Exception): Boolean {
        return when {
            ex is HttpServerErrorException && ex.statusCode == HttpStatus.GATEWAY_TIMEOUT -> true
            ex is ResourceAccessException && (ex.cause is ResourceAccessException) -> true
            else -> false
        }
    }

}