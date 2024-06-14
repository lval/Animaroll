package com.lvalentin.animaroll.common

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.lvalentin.animaroll.models.FolderVm
import java.io.File

object Utils {

    private fun isPathExist(path: String): Boolean {
        return path.isNotEmpty() && File(path).exists()
    }

    fun getPathsFromBucketIds(context: Context, bucketIds: List<String>?): List<FolderVm> {
        return bucketIds?.mapNotNull { bucketId ->
            val contentResolver = context.contentResolver
            val queryUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val selection = "${MediaStore.MediaColumns.BUCKET_ID} = ?"
            val selectionArgs = arrayOf(bucketId)

            contentResolver.query(queryUri, projection, selection, selectionArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val folderUri = cursor.getString(cursor.getColumnIndexOrThrow(projection[0])).substringBeforeLast('/')
                    if (isPathExist(folderUri)) {
                        FolderVm(id = bucketId, path = folderUri)
                    } else null
                } else null
            }
        } ?: emptyList()
    }

    fun getPathsFromPaths(pathIds: List<String>?): List<FolderVm> {
        return pathIds?.mapNotNull { path ->
            if (isPathExist(path)) {
                FolderVm(id = path, path = path)
            } else {
                null
            }
        } ?: emptyList()
    }

    fun verifyAppIntegrity(context: Context) {
        try {
            val tmpFile = File(context.filesDir, "tmp")
            if (tmpFile.createNewFile()) {
                tmpFile.delete()
            }
        } catch (e: Exception) {
            val errMsg = "App dir access check failed"
            Log.e("SecurityCheck", errMsg, e)
            throw SecurityException(errMsg, e)
        }
    }
}