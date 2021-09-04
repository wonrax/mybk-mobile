package com.wonrax.bkstinfo.utils

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient

class OkHttpClientSingleton {
    companion object {
        @Volatile private var isInitiated: Boolean = false
        @Volatile var httpClient: OkHttpClient = OkHttpClient()
        @Volatile private var cookieJar: ClearableCookieJar? = null

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
                    .build()
                isInitiated = true
            }
        }
    }
}
