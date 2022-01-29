package com.wonrax.mybk.network

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wonrax.mybk.BuildConfig
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Needs to be a singleton in order to share a cookie jar, which is needed for MyBK authentication
 * and requests.
 */
object OkHttpClientSingleton {
    @Volatile
    private var isInitiated: Boolean = false

    @Volatile
    lateinit var httpClient: OkHttpClient

    @Volatile
    lateinit var cookieJar: ClearableCookieJar

    fun init(context: Context) {
        val check = isInitiated
        if (check) {
            return
        }
        synchronized(this) {
            // Double check
            val recheck = isInitiated
            if (recheck) {
                return
            }
            cookieJar =
                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
            httpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar as PersistentCookieJar)
                .apply {
                    // Network inspection
                    if (BuildConfig.DEBUG) {
                        addNetworkInterceptor(StethoInterceptor())
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                setLevel(
                                    HttpLoggingInterceptor.Level.BASIC
                                )
                            }
                        )
                    }
                    followRedirects(true)
                    followSslRedirects(false)
                    connectionSpecs(
                        listOf(
                            ConnectionSpec.CLEARTEXT,
                            ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                                .allEnabledTlsVersions()
                                .allEnabledCipherSuites()
                                .build()
                        )
                    )
                }
                .build()
            isInitiated = true
        }
    }
}
