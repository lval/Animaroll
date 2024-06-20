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
import com.lvalentin.animaroll.common.Utils.getPathsFromBucketIds
import com.lvalentin.animaroll.models.FolderVm
import com.lvalentin.animaroll.models.MediaVm
import java.io.File


class MediaActivity: AppCompatActivity() {

    private val mapVm = mutableMapOf<String, MediaVm>()
    private val tempDirIds = ArrayList<String>()
    private var savedDirIds = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.init(this)
        setContentView(R.layout.media_activity)
        supportActionBar?.apply {
            title = getString(R.string.title_media_albums)
            setDisplayHomeAsUpEnabled(true)
        }

        val prefDirs = PreferenceManager.getString(R.string.gpf_dir_media, "", this)
        if (prefDirs.isNotEmpty()) {
            savedDirIds = ArrayList(prefDirs.split(Constant.FOLDER_DELIMITER))
            val validDirs = getPathsFromBucketIds(this, savedDirIds) as ArrayList<FolderVm>
            if (validDirs.size > 0) {
                savedDirIds = ArrayList()
                for (i in validDirs) {
                    savedDirIds.add(i.id)
                }
            }
        }

        getMediaDirectories()

        val mediaVmList = ArrayList<MediaVm>()
        for ((_, value) in mapVm) {
            mediaVmList.add(value)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.rv_media)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val adapter = MediaAdapter(mediaVmList)
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
            editor.putString(getString(R.string.gpf_dir_media), selDirStr)
            editor.apply()
        }
        supportActionBar?.subtitle = this.resources.getQuantityString(
            R.plurals.folders_cnt, adapter.itemCount - 1, adapter.itemCount - 1
        )
    }

    private fun getMediaDirectories() {
        getImageDirectories(this)
        getVideoDirectories(this)
    }

    @SuppressLint("Range")
    private fun getImageDirectories(context: Context) {
        val contentResolver = context.contentResolver
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
        val includeFiles = MediaStore.Images.Media.MIME_TYPE + " LIKE 'image/%' "
        val excludeGif = " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/gif' " + " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/giff' "
        val selection = includeFiles + excludeGif
        val cursor = contentResolver.query(queryUri, projection, selection, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val folderUri = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
                val folderName = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
                val folderId = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))

                if (!tempDirIds.contains(folderId)) {
                    val folderParentUri = File(folderUri).parent!!
                    tempDirIds.add(folderId)
                    val folder = MediaVm(
                        id = folderId,
                        name = folderName,
                        path = folderParentUri,
                        itemsCnt = 1,
                        isSelected = savedDirIds.contains(folderId)
                    )
                    mapVm[folderId] = folder
                } else if (tempDirIds.contains(folderId)) {
                    val mi = mapVm[folderId]
                    mi?.itemsCnt = mi?.itemsCnt?.plus(1)!!
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    @SuppressLint("Range")
    private fun getVideoDirectories(context: Context) {
        val contentResolver = context.contentResolver
        val queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID
        )
        val includeFiles = MediaStore.Video.Media.MIME_TYPE + " LIKE 'video/%' "
        val cursor = contentResolver.query(queryUri, projection, includeFiles, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val folderUri = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
                val folderName = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
                val folderId = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))

                if (!tempDirIds.contains(folderId)) {
                    tempDirIds.add(folderId)
                    val folderParentUri = File(folderUri).parent!!
                    val folder = MediaVm(
                        id = folderId,
                        name = folderName,
                        path = folderParentUri,
                        itemsCnt = 1,
                        isSelected = savedDirIds.contains(folderId)
                    )
                    mapVm[folderId] = folder
                } else if (tempDirIds.contains(folderId)) {
                    val mi = mapVm[folderId]
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
