package com.lvalentin.animaroll.services

import android.graphics.SurfaceTexture
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import com.lvalentin.animaroll.SlideShowActivity


class MediaService(
    private val parent: SlideShowActivity,
    textureView: TextureView,
//    private val screenX: Int,
//    private val screenY: Int,
//    private val prefScale: Int,
): TextureView.SurfaceTextureListener {

    interface OnVideoPreparedListener {
        fun onVideoPrepared()
    }

    var videoPlayer: MediaPlayer? = null
    private var surface: Surface? = null

    var onVideoPreparedListener: OnVideoPreparedListener? = null

    init {
        textureView.surfaceTextureListener = this
    }

    fun prepare(uri: String) {
        if (videoPlayer != null) {
            try {
                videoPlayer?.reset()
                videoPlayer?.setDataSource(uri)
                videoPlayer?.prepareAsync()
//                val audioSessionId = videoPlayer?.audioSessionId
//                val loudnessEnhancer = LoudnessEnhancer(audioSessionId!!)
//                loudnessEnhancer.setTargetGain(1000)
//                loudnessEnhancer.enabled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hasAudioTrack(videoUri: String): Boolean {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(parent, Uri.parse(videoUri))
            val hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)
            val hasAudio = hasAudioStr?.equals("yes", ignoreCase = true) ?: false
            return hasAudio
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return false
    }

    fun play() {
        videoPlayer?.start()
    }

    fun reset() {
        videoPlayer?.stop()
        videoPlayer?.reset()
    }

    fun stop() {
        videoPlayer?.stop()
    }

    fun release() {
        videoPlayer?.stop()
        videoPlayer?.release()
        videoPlayer = null
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        if (videoPlayer == null) {
            try {
                videoPlayer = MediaPlayer()
                videoPlayer?.setSurface(Surface(surfaceTexture))
                videoPlayer?.setOnPreparedListener {
                    onVideoPreparedListener?.onVideoPrepared()
                }
                videoPlayer?.setOnCompletionListener {
                    parent.videoOnCompletionCallback()
                }
                parent.onSurfaceTextureAvailableCallback()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, p1: Int, p2: Int) {
//        try {
//            videoPlayer = MediaPlayer()
//            videoPlayer?.setSurface(Surface(surfaceTexture))
//            videoPlayer?.setOnPreparedListener { mediaPlayer ->
//                onVideoPreparedListener?.onVideoPrepared()
//                val viewWidth: Int = screenX
//                val viewHeight: Int = screenY
//                val aspectRatio: Float = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
//                val screenRatio = viewWidth / viewHeight.toFloat()
//                val scaleX = aspectRatio / screenRatio
//                when (prefScale) {
//                    Enums.PrefMediaFit.COVER.id -> {
//                        if (scaleX >= 1f) {
//                            textureView.scaleX = scaleX
//                        } else {
//                            textureView.scaleY = 1f / scaleX
//                        }
//                    }
//                    Enums.PrefMediaFit.FILL.id -> {}
//                    else -> {
//                        if (scaleX >= 1f) {
//                            textureView.scaleY = 1f / scaleX
//                        } else {
//                            textureView.scaleX = scaleX
//                        }
//                    }
//                }
//            }
//            videoPlayer?.setOnCompletionListener {
//                parent.videoOnCompletionCallback()
//                //showMedia()
//            }
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//        } catch (e: SecurityException) {
//            e.printStackTrace()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        parent.onSurfaceTextureAvailableCallback()
//    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        parent.onSurfaceTextureDestroyedCallback()
        surface?.release()
        return true
    }
    override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
}
