package com.wonrax.bkstinfo.network

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.wonrax.bkstinfo.network.Response as CustomResponse

/**
 * Suspend extension that allows suspend [Call] inside coroutine.
 *
 * [recordStack] enables track recording, so in case of exception stacktrace will contain call stacktrace, may be useful for debugging
 *      Not free! Creates exception on each request so disabled by default, but may be enabled using system properties:
 *
 *      ```
 *      System.setProperty(OKHTTP_STACK_RECORDER_PROPERTY, OKHTTP_STACK_RECORDER_ON)
 *      ```
 *      see [README.md](https://github.com/gildor/kotlin-coroutines-okhttp/blob/master/README.md#Debugging) with details about debugging using this feature
 *
 * @return Result of request or throw exception
 */
public suspend fun Call.await(recordStack: Boolean = false): CustomResponse {
    val callStack = if (recordStack) {
        IOException().apply {
            // Remove unnecessary lines from stacktrace
            // This doesn't remove await$default, but better than nothing
            stackTrace = stackTrace.copyOfRange(1, stackTrace.size)
        }
    } else {
        null
    }
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(
                    CustomResponse(
                        response.body?.string() ?: "",
                        response.headers,
                        response.code
                    )
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                // Don't bother with resuming the continuation if it is already cancelled.
                if (continuation.isCancelled) return
                callStack?.initCause(e)
                continuation.resumeWithException(callStack ?: e)
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                // Ignore cancel exception
            }
        }
    }
}

object Cookuest {
    private val httpClient = OkHttpClientSingleton.httpClient
    private val requestTemplate: Request.Builder = Request.Builder().apply {
        addHeader("Accept", "*/*")
    }

    // TODO Write docs
    fun post(
        url: String,
        requestBody: RequestBody,
    ): Call {
        val request: Request = requestTemplate
            .url(url)
            .post(requestBody)
            .build()
        return httpClient.newCall(request)
    }

    // TODO Write docs
    fun get(
        url: String
    ): Call {
        val request: Request = requestTemplate
            .url(url)
            .get()
            .build()
        return httpClient.newCall(request)
    }
}
