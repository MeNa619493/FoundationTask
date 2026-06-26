package com.example.foundationtask.domain.exception

sealed class DomainException(
    cause: Throwable
) : Exception(cause) {
    class NetworkUnavailableException(cause: Throwable) : DomainException(cause)
    class ServerErrorException(cause: Throwable) : DomainException(cause)
    class ClientErrorException(cause: Throwable) : DomainException(cause)
    class AuthenticationException(cause: Throwable) : DomainException(cause)
    class DataParsingException(cause: Throwable) : DomainException(cause)
    class UnknownSyncException(cause: Throwable) : DomainException(cause)
}