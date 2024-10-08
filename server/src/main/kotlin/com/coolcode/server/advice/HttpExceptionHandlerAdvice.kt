package com.coolcode.server.advice

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import com.coolcode.server.error.*
import com.coolcode.server.dto.ErrorDto
import org.apache.logging.log4j.LogManager

@ControllerAdvice
class HttpExceptionHandlerAdvice {
    companion object {
        private val logger = LogManager.getLogger()
    }

    @ExceptionHandler(HttpException::class)
    fun handleException(e: HttpException): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(e.getStatus()).body(ErrorDto(e.getStatus().value(), e.message ?: ""))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto(HttpStatus.FORBIDDEN.value(), e.message ?: "Unauthorized"))
    }

    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthorizedException(e: UnauthenticatedException): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.message ?: "Unauthenticated"))
    }

    @ExceptionHandler(ValidationException::class)
    fun handleNotFoundException(e: ValidationException): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto(HttpStatus.BAD_REQUEST.value(), e.message ?: "Bad Request"))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDto(HttpStatus.NOT_FOUND.value(), e.message ?: "Not Found"))
    }

    @ExceptionHandler(Throwable::class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun handleThrowable(e: Throwable): ResponseEntity<ErrorDto> {
        logger.error(e.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Some error occurs. Please contact support."))
    }
}