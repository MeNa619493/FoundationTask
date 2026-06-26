package com.example.foundationtask.data.utils

import com.example.foundationtask.data.remote.exceptions.ServiceException.AuthenticationException
import com.example.foundationtask.data.remote.exceptions.ServiceException.ClientErrorException
import com.example.foundationtask.data.remote.exceptions.ServiceException.DataParsingException
import com.example.foundationtask.data.remote.exceptions.ServiceException.NetworkUnavailableException
import com.example.foundationtask.data.remote.exceptions.ServiceException.ServerErrorException
import com.example.foundationtask.data.remote.exceptions.ServiceException.UnknownSyncException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonDecodingException
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalSerializationApi::class)
suspend fun <T> handleRequest(
    remoteCall: suspend () -> T,
    onFailure: (suspend (Throwable) -> Unit)? = null
): T {
    try {
        return remoteCall()
    } catch (e: IOException) {
        val exception = NetworkUnavailableException(cause = e)
        onFailure?.invoke(exception)
        throw exception
    } catch (e: HttpException) {
        val exception = when (e.code()) {
            401 -> AuthenticationException(cause = e)
            in 400..499 -> ClientErrorException(cause = e)
            in 500..599 -> ServerErrorException(cause = e)
            else -> UnknownSyncException(cause = e)
        }
        onFailure?.invoke(exception)
        throw exception
    } catch (e: JsonDecodingException) {
        val exception = DataParsingException(cause = e)
        onFailure?.invoke(exception)
        throw exception
    } catch (e: Throwable) {
        val exception = UnknownSyncException(cause = e)
        onFailure?.invoke(exception)
        throw exception
    }
}