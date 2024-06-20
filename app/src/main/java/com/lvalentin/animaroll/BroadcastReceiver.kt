package com.lvalentin.animaroll

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.PreferenceManager
//import androidx.preference.PreferenceManager

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        PreferenceManager.init(context)

        val startup = PreferenceManager.getBoolean(R.string.gpf_boot, false, context)
        Log.d(Constant.TAG, "** MyBroadcastReceiver: $startup")
        if (startup && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, MainActivity::class.java)
            context.startActivity(i)
        }

//        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val startup = preferences.getBoolean(context.resources.getString(R.string.gpf_boot), false)
//        if (startup && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//            val i = Intent(context, MainActivity::class.java)
//            context.startActivity(i)
//        }
    }
}
