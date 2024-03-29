package com.wonrax.mybk.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

private const val SHARED_PREFERENCES_NAME = "UserCredentials"

object EncryptedStorage {
    private lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context) {
        if (this::sharedPrefs.isInitialized) return
        synchronized(this) {
            // Double check
            if (this::sharedPrefs.isInitialized) {
                return
            }

            // TODO consider remove the Android KeyStore since it is known to be buggy
            // Read: https://github.com/google/tink/issues/504
            // Read: https://github.com/google/tink/issues/413

            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            sharedPrefs = EncryptedSharedPreferences.create(
                SHARED_PREFERENCES_NAME,
                mainKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    // TODO get in batch
    fun get(key: String): String? {
        return sharedPrefs.getString(key, null)
    }

    // TODO set in batch
    fun set(key: String, value: String?) {
        sharedPrefs.edit(commit = true) {
            if (value == null) {
                remove(key)
            } else {
                putString(key, value)
            }
        }
    }

    // TODO get in batch
    fun getBoolean(key: String): Boolean? {
        if (!sharedPrefs.contains(key)) return null
        return sharedPrefs.getBoolean(key, false)
    }

    // TODO set in batch
    fun setBoolean(key: String, value: Boolean?) {
        sharedPrefs.edit(commit = true) {
            if (value == null) {
                remove(key)
            } else {
                putBoolean(key, value)
            }
        }
    }
}
