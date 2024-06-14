package com.lvalentin.animaroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)
        supportActionBar?.apply {
            title = getString(R.string.title_setting)
            elevation = 0F
            setDisplayHomeAsUpEnabled(true)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_layout, SettingsFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
