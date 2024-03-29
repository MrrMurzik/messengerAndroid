package com.example.messengerAndroid.app.foundation.exceptions

open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class AccountAlreadyExistsException(
    cause: Throwable
) : AppException(cause = cause)

//class AuthException(
//    cause: Throwable
//) : AppException(cause = cause)

//class InvalidCredentialsException(cause: Exception) : AppException(cause = cause)
//
class ConnectionException(cause: Throwable) : AppException(cause = cause)

open class BackendException(
    val code: Int,
    message: String
) : AppException(message)

class ParseBackendResponseException(
    cause: Throwable
) : AppException(cause = cause)