package br.com.winestore.exception

import org.springframework.http.HttpStatus

class BaseException (
    val messageError: String,
    val httpStatus: HttpStatus,
) : RuntimeException()