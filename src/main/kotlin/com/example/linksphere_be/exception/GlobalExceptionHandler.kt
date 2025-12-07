package com.example.linksphere_be.exception

import com.example.linksphere_be.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
            ex: NoSuchElementException,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.NOT_FOUND
        val errorResponse =
                ErrorResponse(
                        timestamp = LocalDateTime.now(),
                        status = status.value(),
                        error = status.reasonPhrase,
                        message = ex.message ?: "Resource not found",
                        path = request.requestURI
                )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
            ex: IllegalArgumentException,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val errorResponse =
                ErrorResponse(
                        timestamp = LocalDateTime.now(),
                        status = status.value(),
                        error = status.reasonPhrase,
                        message = ex.message ?: "Invalid argument",
                        path = request.requestURI
                )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
            ex: MethodArgumentNotValidException,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val message =
                ex.bindingResult.allErrors.joinToString(", ") {
                    it.defaultMessage ?: "Invalid value"
                }
        val errorResponse =
                ErrorResponse(
                        timestamp = LocalDateTime.now(),
                        status = status.value(),
                        error = status.reasonPhrase,
                        message = message,
                        path = request.requestURI
                )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
            ex: Exception,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val errorResponse =
                ErrorResponse(
                        timestamp = LocalDateTime.now(),
                        status = status.value(),
                        error = status.reasonPhrase,
                        message = ex.message ?: "Internal server error",
                        path = request.requestURI
                )
        return ResponseEntity.status(status).body(errorResponse)
    }
}
