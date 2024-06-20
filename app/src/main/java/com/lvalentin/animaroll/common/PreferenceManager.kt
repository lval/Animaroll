package com.lvalentin.animaroll.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceManager {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getEditor(): SharedPreferences.Editor {
        return preferences.edit()
    }

    fun getString(keyResId: Int, defaultValue: String, context: Context): String {
        val key = context.getString(keyResId)
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getBoolean(keyResId: Int, defaultValue: Boolean, context: Context): Boolean {
        val key = context.getString(keyResId)
        return preferences.getBoolean(key, defaultValue)
    }

    fun getInt(keyResId: Int, defaultValue: Int, context: Context): Int {
        val key = context.getString(keyResId)
        val value = preferences.all[key]
        return when (value) {
            is String -> value.toIntOrNull() ?: defaultValue
            is Int -> value
            else -> defaultValue
        }
    }

    fun getLong(keyResId: Int, defaultValue: Long, context: Context): Long {
        val key = context.getString(keyResId)
        val value = preferences.all[key]
        return when (value) {
            is String -> value.toLongOrNull() ?: defaultValue
            is Long -> value
            else -> defaultValue
        }
    }
}