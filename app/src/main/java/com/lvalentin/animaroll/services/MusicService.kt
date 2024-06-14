package com.lvalentin.animaroll.services

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.lvalentin.animaroll.R
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.PreferenceManager
import java.io.IOException


class MusicService(private val context: Context, private val songList: List<Uri>) {
    private var musicPlayer: MediaPlayer? = null
    private val musicHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var songIx: Int = 0
    private var isRepeat: Boolean = false

    init {
        PreferenceManager.init(context)

        if (musicPlayer == null && songList.isNotEmpty()) {
            try {
                val storedTrack = loadStoredTrack()
                if (storedTrack > 0 && storedTrack < songList.size) {
                    Log.d(Constant.TAG, "MusicPlayer: Stored song $storedTrack")
                    songIx = storedTrack
                } else {
                    Log.d(Constant.TAG, "MusicPlayer: Default song $songIx")
                }

                musicPlayer = MediaPlayer()
                musicPlayer?.setOnCompletionListener {
                    next()
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadStoredTrack(): Int {
        val prefs = PreferenceManager.getPreferences()
        return prefs.getInt(context.getString(R.string.spf_music_ix), 0)
    }

    private fun saveCurrentTrack() {
        val editor = PreferenceManager.getEditor()
        editor.putInt(context.getString(R.string.spf_music_ix), songIx)
        editor.apply()
    }

    fun play() {
        if (songList.isNotEmpty() && songIx >= 0 && songIx < songList.size) {
            musicPlayer?.reset()
            musicPlayer?.setDataSource(context, songList[songIx])
            musicPlayer?.prepare()

//            val audioSessionId = musicPlayer?.audioSessionId
//            val loudnessEnhancer = LoudnessEnhancer(audioSessionId!!)
//            loudnessEnhancer.setTargetGain(1000)
//            loudnessEnhancer.enabled = true

            musicPlayer?.start()
        }
    }

    fun resume() {
        if (songList.isNotEmpty() && musicPlayer?.isPlaying == false) {
            musicPlayer?.setVolume(0f, 0f)
            musicPlayer?.start()

            val maxVolume = 1.0f
            var volume = 0.0f
            musicHandler.post(object: Runnable {
                override fun run() {
                    if (volume < maxVolume) {
                        volume += maxVolume / (Constant.MUSIC_FADE_DURATION / 100).toFloat()
                        musicPlayer?.setVolume(volume, volume)
                        musicHandler.postDelayed(this, Constant.MUSIC_FADE_INTERVAL)
                    } else {
                        musicPlayer?.setVolume(maxVolume, maxVolume)
                    }
                }
            })
        }
    }

    fun pause() {
        if (songList.isNotEmpty() && musicPlayer?.isPlaying == true) {
            musicPlayer?.pause()
        }
    }

    fun repeat(repeatYN: Boolean = false) {
        isRepeat = repeatYN
    }

    fun next(isForced: Boolean = false) {
        if (!isRepeat || isForced) {
            songIx += 1
            if (songIx >= songList.size) {
                songIx = 0
            }
            saveCurrentTrack()
        }
        play()
    }

    fun previous(isForced: Boolean = false) {
        if (!isRepeat || isForced) {
            songIx -= 1
            if (songIx < 0) {
                songIx = songList.size - 1
            }
            saveCurrentTrack()
        }
        play()
    }

    fun release() {
        Log.d(Constant.TAG, "MusicPlayer: Released")
        if (songList.isNotEmpty() && musicPlayer != null) {
            musicPlayer?.stop()
            musicPlayer?.release()
        }
        musicPlayer = null
        musicHandler.removeCallbacksAndMessages(null)
    }
}
