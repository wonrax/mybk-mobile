package com.wonrax.mybk

import android.app.Application
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
import com.wonrax.mybk.models.DeviceUser
import com.wonrax.mybk.models.EncryptedStorage
import com.wonrax.mybk.network.OkHttpClientSingleton

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable network inspection
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        // Init singleton to enable cookies
        OkHttpClientSingleton.init(this)

        EncryptedStorage.init(this)
        DeviceUser.init() // This must comes after EncryptedStorage (dependency)
    }
}
