package com.example.foundationtask.data.mappers

import com.example.foundationtask.data.remote.exceptions.ServiceException
import com.example.foundationtask.data.remote.exceptions.ServiceException.NetworkUnavailableException
import com.example.foundationtask.data.remote.exceptions.ServiceException.UnknownSyncException
import com.example.foundationtask.data.remote.exceptions.ServiceException.ServerErrorException
import com.example.foundationtask.data.remote.exceptions.ServiceException.DataParsingException
import com.example.foundationtask.data.remote.exceptions.ServiceException.AuthenticationException
import com.example.foundationtask.data.remote.exceptions.ServiceException.ClientErrorException
import com.example.foundationtask.domain.exception.DomainException

fun ServiceException.toDomainException(): DomainException {
    return when (this) {
        is NetworkUnavailableException -> DomainException.NetworkUnavailableException(cause = rootCause)
        is AuthenticationException -> DomainException.AuthenticationException(cause = rootCause)
        is ClientErrorException -> DomainException.ClientErrorException(cause = rootCause)
        is DataParsingException -> DomainException.DataParsingException(cause = rootCause)
        is ServerErrorException -> DomainException.ServerErrorException(cause = rootCause)
        is UnknownSyncException -> DomainException.UnknownSyncException(cause = rootCause)
    }
}