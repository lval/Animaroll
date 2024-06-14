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
}