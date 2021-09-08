package com.wonrax.bkstinfo.network

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException
import com.wonrax.bkstinfo.network.Response as CustomResponse

object Cookuest {
    private val httpClient = OkHttpClientSingleton.httpClient
    private val requestTemplate: Request.Builder = Request.Builder().apply {
        addHeader("Accept", "*/*")
    }

    @Throws(IOException::class)
    fun post(
        url: String,
        requestBody: RequestBody,
        onError: ((IOException) -> Unit)? = null,
        onResponse: ((CustomResponse) -> Unit)? = null
    ): String {
        val request: Request = requestTemplate
            .url(url)
            .post(requestBody)
            .build()
        httpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (onError != null) onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val returnResponse = CustomResponse(
                        response.body?.string() ?: "",
                        response.headers,
                        response.code
                    )
                    if (onResponse != null) onResponse(returnResponse)
                }
            }
        )
        httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
    }

    @Throws(IOException::class)
    fun get(
        url: String,
        onResponse: ((CustomResponse) -> Unit)? = null,
        onError: ((IOException) -> Unit)? = null
    ) {
        val request: Request = requestTemplate
            .url(url)
            .get()
            .build()
        httpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (onError != null) onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val returnResponse = CustomResponse(
                        response.body?.string() ?: "",
                        response.headers,
                        response.code
                    )
                    if (onResponse != null) onResponse(returnResponse)
                }
            }
        )
    }
}
