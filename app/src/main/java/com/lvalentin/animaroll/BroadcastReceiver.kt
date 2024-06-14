package com.lvalentin.animaroll

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager


class BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val startup = preferences.getBoolean(context.resources.getString(R.string.pfk_boot), false)
        if (startup && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, MainActivity::class.java)
            context.startActivity(i)
        }
    }
}
