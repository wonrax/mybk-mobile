package com.example.bkstinfo.utils

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient

class OkHttpClientSingleton {
    companion object {
        @Volatile private var INSTANCE: OkHttpClientSingleton? = null
        @Volatile var httpClient: OkHttpClient = OkHttpClient()
        @Volatile private var cookieJar: ClearableCookieJar? = null

        fun init(context: Context) {
            val instance = INSTANCE
            if (instance != null) {
                return
            }
            synchronized(this) {
                // Double check
                val recheckInstance = INSTANCE
                if (recheckInstance != null) {
                    return
                }
                val newInstance = OkHttpClientSingleton()
                OkHttpClientSingleton.cookieJar =
                    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
                OkHttpClientSingleton.httpClient = OkHttpClient.Builder()
                    .cookieJar(cookieJar as PersistentCookieJar)
                    .build()
            }
        }
    }
}

