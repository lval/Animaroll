package com.lvalentin.animaroll

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.util.Calendar


class SettingsFragment: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.OnPreferenceClickListener {

    companion object {
        private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
        private const val PLAY_MARKET_URL = "market://details?id="
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val sharedPreferences = preferenceManager.sharedPreferences
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

        findPreference<Preference>("app_version")?.let { appVersion ->
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            appVersion.title = "Version ${BuildConfig.VERSION_NAME}"
            appVersion.summary = getString(R.string.app_copy_summary, currentYear)
        }

        findPreference<Preference>("app_rate")?.let { appRate ->
            appRate.setOnPreferenceClickListener {
                context?.let { ctx ->
                    try {
                        val uri = Uri.parse("$PLAY_MARKET_URL${ctx.packageName}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        ctx.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val webUri = Uri.parse("$PLAY_STORE_URL${ctx.packageName}")
                        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                        ctx.startActivity(webIntent)
                    }
//                    var reviewFlowLaunched = false
//                    val manager = ReviewManagerFactory.create(ctx)
//                    val request = manager.requestReviewFlow()
//                    request.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val reviewInfo = task.result
//                            val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
//                            flow.addOnCompleteListener { _ ->
//                                Log.d(Constant.TAG, "addOnCompleteListener")
//                                reviewFlowLaunched = true
//
//                                val builder = AlertDialog.Builder(ctx)
//                                builder.setTitle("Thank You!")
//                                builder.setMessage("Did you submit a review?")
//                                builder.setPositiveButton("Yes") { _, _ ->
//                                }
//                                builder.setNegativeButton("No") { dialog, _ ->
//                                    dialog.dismiss()
//                                }
//                                builder.show()
//                            }
//                        }
//                    }
                }
                true
            }
        }

        findPreference<Preference>("app_share")?.let { appShare ->
            appShare.setOnPreferenceClickListener {
                context?.let { ctx ->
                    val appName = getString(R.string.app_name)
                    val webUri = Uri.parse("${PLAY_STORE_URL}${ctx.packageName}")
                    val shareText = getString(R.string.app_share_msg, appName, webUri.toString())
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    ctx.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
                true
            }
        }

        findPreference<Preference>("app_website")?.let { appWebsite ->
            appWebsite.setOnPreferenceClickListener {
                context?.let { ctx ->
                    val webUri = Uri.parse(getString(R.string.app_website_url))
                    val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                    ctx.startActivity(webIntent)
                }
                true
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {}
    override fun onPreferenceClick(preference: Preference): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}
