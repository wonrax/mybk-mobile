package com.wonrax.bkstinfo

import android.app.Application
import com.wonrax.bkstinfo.utils.OkHttpClientSingleton

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Init singleton to enable cookies
        OkHttpClientSingleton.init(applicationContext)
    }
}
