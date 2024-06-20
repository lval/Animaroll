package com.lvalentin.animaroll

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.PreferenceManager
import com.lvalentin.animaroll.common.Utils
import com.lvalentin.animaroll.models.FolderVm
import com.lvalentin.animaroll.models.MusicVm


class MusicActivity: AppCompatActivity() {

    private val mapVm = mutableMapOf<String, MusicVm>()
    private val tempDirIds = ArrayList<String>()
    private var savedDirIds = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.init(this)
        setContentView(R.layout.music_activity)
        supportActionBar?.apply {
            title = getString(R.string.title_music_albums)
            setDisplayHomeAsUpEnabled(true)
        }

        val prefDirs = PreferenceManager.getString(R.string.gpf_dir_music, "", this)
        if (prefDirs.isNotEmpty()) {
            savedDirIds = ArrayList(prefDirs.split(Constant.FOLDER_DELIMITER))
            val validDirs = Utils.getPathsFromPaths(savedDirIds) as ArrayList<FolderVm>
            if (validDirs.size > 0) {
                savedDirIds = ArrayList()
                for (i in validDirs) {
                    savedDirIds.add(i.id)
                }
            }
        }

        getMediaDirectories()

        val mediaVmList = ArrayList<MusicVm>()
        for ((_, value) in mapVm) {
            mediaVmList.add(value)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.rv_music)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val adapter = MusicAdapter(mediaVmList)
        recyclerview.adapter = adapter
        adapter.onItemCheck = { it ->
            val idLower = it.id.lowercase()
            if (it.isSelected) {
                savedDirIds.removeAll { savedId -> savedId.equals(idLower, ignoreCase = true) }
                savedDirIds.add(it.id)
            } else {
                savedDirIds.removeIf { savedId -> savedId.equals(idLower, ignoreCase = true) }
            }

            val selDirStr = savedDirIds.distinctBy { it.lowercase() }
                .joinToString(separator = Constant.FOLDER_DELIMITER, limit = Constant.FOLDER_LIMIT)

            val editor = PreferenceManager.getEditor()
            editor.putString(getString(R.string.gpf_dir_music), selDirStr)
            editor.apply()
        }
        supportActionBar?.subtitle = this.resources.getQuantityString(
            R.plurals.folders_cnt, adapter.itemCount - 1, adapter.itemCount - 1
        )
    }

    private fun getMediaDirectories() {
        getMusicDirectories(this)
    }

    @SuppressLint("Range")
    private fun getMusicDirectories(context: Context) {
        val contentResolver = context.contentResolver
        val queryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA
        )
        val cursor = contentResolver.query(queryUri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val folderUri = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
                val folderPath = folderUri.substringBeforeLast("/")
                val folderName = folderPath.substringAfterLast("/")

                if (!tempDirIds.contains(folderPath)) {
                    tempDirIds.add(folderPath)
                    val folder = MusicVm(
                        id = folderPath,
                        name = folderName,
                        path = folderPath,
                        itemsCnt = 1,
                        isSelected = savedDirIds.contains(folderPath)
                    )
                    mapVm[folderPath] = folder
                } else if (tempDirIds.contains(folderPath)) {
                    val mi = mapVm[folderPath]
                    mi?.itemsCnt = mi?.itemsCnt?.plus(1)!!
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
