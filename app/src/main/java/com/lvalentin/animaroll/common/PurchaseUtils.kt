package com.lvalentin.animaroll.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.lvalentin.animaroll.R


object PurchaseUtils {
    private fun getSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.enc_pref),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun storePurchaseState(context: Context, hasNoAds: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putBoolean(context.getString(R.string.epfk_no_ads), hasNoAds)
            apply()
        }
    }

    fun isNoAdsPurchased(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(context.getString(R.string.epfk_no_ads), false)
    }
}