package com.lvalentin.animaroll

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.android.billingclient.api.ProductDetails
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.PreferenceManager
import com.lvalentin.animaroll.common.PurchaseUtils
import com.lvalentin.animaroll.common.Utils.getPathsFromBucketIds
import com.lvalentin.animaroll.common.Utils.getPathsFromPaths
import com.lvalentin.animaroll.common.Utils.verifyAppIntegrity
import com.lvalentin.animaroll.models.FolderVm
import com.lvalentin.animaroll.services.BillingService
import com.lvalentin.animaroll.services.BillingUpdatesListener


class MainActivity: AppCompatActivity(), BillingUpdatesListener {
    private lateinit var billingService: BillingService
    private lateinit var btnMedia: Button
    private lateinit var btnMusic: Button
    private lateinit var btnBuy: Button

    private var requiredPermissions = ArrayList<String>()
    private var deniedPermissions = ArrayList<String>()

    private var prefAutostart: Boolean = false
    private var prefDirMedia: String = ""
    private var prefDirMusic: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.init(this)
        setContentView(R.layout.main_activity)
        verifyAppIntegrity(this)
        billingService = BillingService(this, this)

        if (findViewById<BottomNavigationView>(R.id.bottom_navigation) != null) {
            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            for (i in 0 until bottomNavView.menu.size()) {
                bottomNavView.menu.getItem(i).isChecked = false
                bottomNavView.menu.getItem(i).setCheckable(false)
            }
            bottomNavView.clearFocus()
            bottomNavView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_settings -> {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_exit -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        } else if (findViewById<NavigationRailView>(R.id.navigation_rail) != null) {
            val railNavView = findViewById<NavigationRailView>(R.id.navigation_rail)
            for (i in 0 until railNavView.menu.size()) {
                railNavView.menu.getItem(i).isChecked = false
                railNavView.menu.getItem(i).setCheckable(false)
            }
            railNavView.clearFocus()
            railNavView.setOnItemSelectedListener  { item ->
                when (item.itemId) {
                    R.id.nav_settings -> {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_exit -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requiredPermissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            requiredPermissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            requiredPermissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            requiredPermissions.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            requiredPermissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            requiredPermissions.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //
        }
        else {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            if (deniedPermissions.size == 0) {
                val intent = Intent(this, SlideShowActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissions(deniedPermissions.toTypedArray(), Constant.PERMISSION_REQUEST_CODE)
            }
        }
        btnStart.requestFocus()

        btnMedia = findViewById(R.id.btn_media)
        btnMedia.setOnClickListener {
            if (deniedPermissions.size == 0) {
                val intent = Intent(this, MediaActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissions(deniedPermissions.toTypedArray(), Constant.PERMISSION_REQUEST_CODE)
            }
        }

        btnMusic = findViewById(R.id.btn_music)
        btnMusic.setOnClickListener {
            if (deniedPermissions.size == 0) {
                val intent = Intent(this, MusicActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissions(deniedPermissions.toTypedArray(), Constant.PERMISSION_REQUEST_CODE)
            }
        }

        btnBuy = findViewById(R.id.btn_buy)
        updateBuyButtonVisibility()

//        val btnServerConn = findViewById<Button>(R.id.btn_server_conn)
//        btnServerConn.setOnClickListener {
//            if (deniedPermissions.size == 0) {
//                val intent = Intent(this, ServerConnActivity::class.java)
//                startActivity(intent)
//            } else {
//                requestPermissions(deniedPermissions.toTypedArray(), Constant.PERMISSION_REQUEST_CODE)
//            }
//        }
//        val button = findViewById<Button>(R.id.btn_server_conn2_music)
//        button.setOnClickListener { showPopupMenu(it) }

        getPreferences()
        checkPermissions()
        getStartData()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onProductDetailsUpdated(productDetails: ProductDetails) {
        btnBuy.text = getString(R.string.btn_buy)
        btnBuy.setOnClickListener {
            billingService.launchPurchaseFlow(this, productDetails)
        }
        updateBuyButtonVisibility()
    }

    override fun onPurchaseStateChanged() {
        //Log.d(Constant.TAG, "onPurchaseStateChanged: ${getPurchaseState()}")
        updateBuyButtonVisibility()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedList = ArrayList<String>()
        when (requestCode) {
            Constant.PERMISSION_REQUEST_CODE -> {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedList.add(permissions[i])
                    }
                }

                if (deniedList.size == 0) {
                    deniedPermissions = ArrayList()
                    getStartData()
                } else {
                    showPermissionDialog(this)
                }
            }
        }
    }

    private fun updateBuyButtonVisibility() {
        runOnUiThread {
            btnBuy.visibility = when (getPurchaseState()) {
                true -> View.GONE
                false -> View.VISIBLE
            }
        }
    }

    private fun getStartData() {
        if (deniedPermissions.size == 0) {
            getMediaCnt(prefDirMedia)
            getMusicCnt(prefDirMusic)
        }
    }

    private fun getPreferences() {
        val preferences = PreferenceManager.getPreferences()
        prefAutostart = preferences.getBoolean(getString(R.string.pfk_startup), false)
        prefDirMedia = preferences.getString(getString(R.string.pfk_dir_media), "") ?: ""
        prefDirMusic = preferences.getString(getString(R.string.pfk_dir_music), "") ?: ""
    }

    private fun getMediaCnt(pDirs: String) {
        var foldersCnt = 0

        if (pDirs.isNotEmpty()) {
            val savedDirs = ArrayList(pDirs.split(Constant.FOLDER_DELIMITER))
            val validDirs = getPathsFromBucketIds(this, savedDirs) as ArrayList<FolderVm>
            foldersCnt = validDirs.size
        }

        btnMedia.text =
            HtmlCompat.fromHtml(
                "${getString(R.string.btn_media)}<br/><small>${this.resources.getQuantityString(R.plurals.albums_cnt, foldersCnt, foldersCnt)}</small>"
                , HtmlCompat.FROM_HTML_MODE_LEGACY
            )
    }

    private fun getMusicCnt(pDirs: String) {
        var foldersCnt = 0

        if (pDirs.isNotEmpty()) {
            val savedDirs = ArrayList(pDirs.split(Constant.FOLDER_DELIMITER))
            val validDirs = getPathsFromPaths(savedDirs) as ArrayList<FolderVm>
            foldersCnt = validDirs.size
        }

        btnMusic.text =
            HtmlCompat.fromHtml(
                "${getString(R.string.btn_music)}<br/><small>${this.resources.getQuantityString(R.plurals.albums_cnt, foldersCnt, foldersCnt)}</small>"
                , HtmlCompat.FROM_HTML_MODE_LEGACY
            )
    }

    private fun checkPermissions() {
        deniedPermissions = ArrayList()
        for (i in requiredPermissions) {
            val permission = ContextCompat.checkSelfPermission(this, i)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(i)
            }
        }

        if (deniedPermissions.size == 0) {
            if (prefAutostart) {
                val intent = Intent(this, SlideShowActivity::class.java)
                startActivity(intent)
            }
        } else {
            requestPermissions(deniedPermissions.toTypedArray(), Constant.PERMISSION_REQUEST_CODE)
        }
    }

    private fun showPermissionDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle(getString(R.string.err_permission_title))
            setMessage(getString(R.string.err_permission_desc, getString(R.string.app_name)))
            setPositiveButton(getString(R.string.err_permission_btn_pos)) { dialog, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.err_permission_btn_neg)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun getPurchaseState(): Boolean {
        return PurchaseUtils.isNoAdsPurchased(this)
    }

    override fun onStart() {
        super.onStart()
        getPreferences()
        getStartData()
        updateBuyButtonVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        billingService.endConnection()
    }
}
