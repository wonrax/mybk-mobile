package com.example.bkstinfo.utils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

object Cookuest {
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private val httpClient = OkHttpClientSingleton.httpClient
    private var requestTemplate: Request.Builder = Request.Builder()
    @Throws(IOException::class)
    fun post(url: String, json: String?): String {
        val body: RequestBody = json?.toRequestBody(JSON) ?: "".toRequestBody()
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        httpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        println(response.body!!.string())
                    }
                }
            }
        )
        httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
    }

    @Throws(IOException::class)
    fun get(url: String, onResponse: ((Response) -> Unit)? = null, onError: ((IOException) -> Unit)? = null): Unit {
        val request: Request = requestTemplate
            .url(url)
            .get()
            .build()
        httpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    if (onError != null) onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        println(response.body!!.string())

                        if (onResponse != null) onResponse(response)
                    }
                }
            }
        )
    }
}
