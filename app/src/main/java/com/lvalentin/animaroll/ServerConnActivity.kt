//package com.lvalentin.animaroll
//
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.lvalentin.animaroll.common.TAG
////import com.lvalentin.animaroll.services.FtpService
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//
//class ServerConnActivity: AppCompatActivity() {
//
//    @OptIn(DelicateCoroutinesApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.music_activity)
//
//        supportActionBar!!.title = getString(R.string.title_music_albums)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val ftpService = FtpService("ftp.example.com", "username", "password")
//
//            try {
//                ftpService.login()
//                val remoteBaseDirectory = "/"
//                val foldersWithImages = ftpService.getFoldersWithImages(remoteBaseDirectory)
//
//                if (foldersWithImages.isNotEmpty()) {
//                    println("Folders with images:")
//                    for (folder in foldersWithImages) {
//                        Log.d(TAG, "$folder")
//                    }
//                } else {
//                    Log.d(TAG, "No folders found with images.")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                ftpService.logout()
//                ftpService.disconnect()
//            }
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        finish()
//        return true
//    }
//}
