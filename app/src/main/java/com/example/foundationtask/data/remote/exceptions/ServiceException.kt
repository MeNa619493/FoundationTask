package com.example.foundationtask.data.remote.exceptions

sealed class ServiceException(val rootCause: Throwable) : Exception(rootCause) {
    class NetworkUnavailableException(cause: Throwable) : ServiceException(cause)
    class AuthenticationException(cause: Throwable) : ServiceException(cause)
    class ClientErrorException(cause: Throwable) : ServiceException(cause)
    class ServerErrorException(cause: Throwable) : ServiceException(cause)
    class UnknownSyncException(cause: Throwable) : ServiceException(cause)
    class DataParsingException(cause: Throwable) : ServiceException(cause)
}