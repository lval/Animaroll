package com.lvalentin.animaroll.services

import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import com.lvalentin.animaroll.SlideShowActivity


class MediaService(private val parent: SlideShowActivity, textureView: TextureView): TextureView.SurfaceTextureListener {

    interface OnVideoPreparedListener {
        fun onVideoPrepared()
    }

    private var surface: Surface? = null
    var videoPlayer: MediaPlayer? = null
    var onVideoPreparedListener: OnVideoPreparedListener? = null

    init {
        textureView.surfaceTextureListener = this
    }

    fun prepare(uri: String) {
        videoPlayer?.reset()
        try {
//            videoPlayer?.setDataSource(uri)
            videoPlayer?.setDataSource(parent, Uri.parse(uri))
            videoPlayer?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hasAudioTrack(videoUri: String): Boolean {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(parent, Uri.parse(videoUri))
            val hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)
            hasAudioStr?.equals("yes", ignoreCase = true) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            retriever.release()
        }
    }

    fun captureFrame(videoUri: Uri, timeUs: Long = 1000000L): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(parent, videoUri)
            retriever.getFrameAtTime(timeUs)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    fun play() {
        videoPlayer?.start()
    }

    fun pause() {
        videoPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun reset() {
        videoPlayer?.stop()
        videoPlayer?.reset()
    }

    fun release() {
        videoPlayer?.release()
        videoPlayer = null
        surface?.release()
        surface = null
    }

    fun pauseAudio() {
        videoPlayer?.setVolume(0f, 0f)
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        if (videoPlayer == null) {
            videoPlayer = MediaPlayer().apply {
                setSurface(Surface(surfaceTexture))
                setOnPreparedListener {
                    onVideoPreparedListener?.onVideoPrepared()
                }
                setOnCompletionListener {
                    parent.videoOnCompletionCallback()
                }
            }
        } else {
            videoPlayer?.setSurface(Surface(surfaceTexture))
        }
        parent.onSurfaceTextureAvailableCallback()
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        parent.onSurfaceTextureDestroyedCallback()
        surface?.release()
        return true
    }
    override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
}
