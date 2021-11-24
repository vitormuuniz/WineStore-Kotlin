package br.com.winestore.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseExceptionHandler {
    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(ex: BaseException): ResponseEntity<String> {
        return ResponseEntity.status(ex.httpStatus).body(ex.messageError);
    }
}