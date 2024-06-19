package com.lvalentin.animaroll.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceManager {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getPreferences(): SharedPreferences {
        return preferences
    }

    fun getEditor(): SharedPreferences.Editor {
        return preferences.edit()
    }

    fun getStringPreference(keyResId: Int, defaultValue: String, context: Context): String {
        val key = context.getString(keyResId)
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getIntPreference(keyResId: Int, defaultValue: Int, context: Context): Int {
        val key = context.getString(keyResId)
        val stringValue = preferences.getString(key, defaultValue.toString())
        return stringValue?.toIntOrNull() ?: defaultValue
    }

    fun getLongPreference(keyResId: Int, defaultValue: Long, context: Context): Long {
        val key = context.getString(keyResId)
        val stringValue = preferences.getString(key, defaultValue.toString())
        return stringValue?.toLongOrNull() ?: defaultValue
    }
}