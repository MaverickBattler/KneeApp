package ru.leti.kneeapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ru.leti.kneeapp.R

class SharedPreferencesProvider (private val applicationContext: Context) {
    fun getEncryptedSharedPreferences(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            applicationContext,
            applicationContext.getString(R.string.shared_preferences_file_name),
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
}