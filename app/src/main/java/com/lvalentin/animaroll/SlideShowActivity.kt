package com.lvalentin.animaroll

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.UiModeManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import androidx.core.os.HandlerCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.LoadAdError
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.Enums
import com.lvalentin.animaroll.common.PreferenceManager
import com.lvalentin.animaroll.common.PurchaseUtils
import com.lvalentin.animaroll.common.Utils
import com.lvalentin.animaroll.models.FolderVm
import com.lvalentin.animaroll.services.AdService
import com.lvalentin.animaroll.services.MediaService
import com.lvalentin.animaroll.services.MusicService
import java.io.File
import java.net.URLConnection
import kotlin.math.abs


class SlideShowActivity: AppCompatActivity(), MediaService.OnVideoPreparedListener {
    private lateinit var lblMsg: LinearLayout
    private lateinit var adService: AdService
    private lateinit var glide: RequestManager
    private lateinit var imageView: ImageView
    private lateinit var textureView: TextureView
    private lateinit var animIn: Animation

    private var mediaService: MediaService? = null
    private var musicService: MusicService? = null

    private val sysBarHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val hideSystemBarsRunnable = Runnable { hideSystemBars() }
    private var winInsetsController: WindowInsetsControllerCompat? = null

    private var animLst: Map<String, Int> = emptyMap()
    private var isRandomTransition: Boolean = false
    private var mediaIx: Int = 0
    private var mediaLst = ArrayList<String>()
    private var prevContentType: Enums.MediaType? = null
    private var disableInputs: Boolean = false
    private var isMusicRepeat: Boolean = false
    private var hasMediaAudio: Boolean = false
    private var timerImage: CountDownTimer? = null
    private var isSysBarsVisible = false
    private var isTelevision = false
    private var screenX: Int = 0
    private var screenY: Int = 0

    private var prefMediaType: Int = 1
    private var prefScale: Int = 1
    private var prefDuration: Long = 15
    private var prefDirMedia: String = ""
    private var prefDirMusic: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.init(this)
        setContentView(R.layout.slideshow_activity)

        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        isTelevision = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            hide()
        }

        glide = Glide.with(this@SlideShowActivity)

        val adLayout = findViewById<View>(R.id.ad_layout)
        adService = AdService(this, adLayout, isTelevision)

        val hasNoAds: Boolean = PurchaseUtils.isNoAdsPurchased(this)
        Log.d(Constant.TAG, "** HasNoAds: $hasNoAds")
        if (!hasNoAds) {
            adService.setAdListener(object: AdService.AdEventListener {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(Constant.TAG, "Failed to load ad: ${error.message}")
                    showMedia()
                }

                override fun onAdLoaded() {
                    disableInputs = true
                    pauseMedia()
                }

                override fun onAdClicked() {
                    Log.d(Constant.TAG, "onAdClicked")
                }

                override fun onAdClosed() {
                    disableInputs = true
                    showMedia()
                }

                override fun onAddVideoEnd() {
                    showMedia()
                }
            })
            adService.initAd()
        }

        val btnInfo = toolbar.findViewById<ImageButton>(R.id.btn_info)
        btnInfo.setOnClickListener {
            showHelpDialog()
        }

        val btnRepeat = toolbar.findViewById<ImageButton>(R.id.btn_repeat)
        btnRepeat.setOnClickListener {
            TooltipCompat.setTooltipText(btnRepeat, getString(R.string.tooltip_repeat))
            isMusicRepeat = !isMusicRepeat
            musicService?.repeat(isMusicRepeat)
            if (isMusicRepeat) {
                btnRepeat.setImageResource(R.drawable.ic_repeat_one_on)
            } else {
                btnRepeat.setImageResource(R.drawable.ic_repeat_one_off)
            }

            showHideSystemBarsWithDelay()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            winInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            winInsetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT

            val metrics: WindowMetrics = this.getSystemService(WindowManager::class.java).currentWindowMetrics
            screenX =  metrics.bounds.width()
            screenY =  metrics.bounds.height()
        } else {
            val display = this.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if (display != null) {
                DisplayMetrics().also { display.getRealMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            screenX = metrics.widthPixels
            screenY = metrics.heightPixels
        }

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val isNavBarsVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())
            if (!isTelevision) {
                if (isNavBarsVisible) {
                    supportActionBar?.show()
                } else {
                    supportActionBar?.hide()
                }
            }
            insets
        }

        hideSystemBars()

        val preferences = PreferenceManager.getPreferences()
        val mediaTypeString = preferences.getString(getString(R.string.pfk_media_type), prefMediaType.toString())
        prefMediaType = mediaTypeString?.toIntOrNull() ?: prefMediaType
        val scaleString = preferences.getString(getString(R.string.pfk_media_scale), prefScale.toString())
        prefScale = scaleString?.toIntOrNull() ?: prefScale
        val prefTransitionDefault: Int = R.integer.pref_media_transition_default
        val transitionString = preferences.getString(getString(R.string.pfk_media_transition), prefTransitionDefault.toString())
        val prefTransition = transitionString?.toIntOrNull() ?: prefTransitionDefault
        val durationString = preferences.getString(getString(R.string.pfk_img_duration), prefDuration.toString())
        prefDuration = durationString?.toLongOrNull()?.times(1000) ?: (prefDuration * 1000)
        prefDirMedia = preferences.getString(getString(R.string.pfk_dir_media), "") ?: ""
        prefDirMusic = preferences.getString(getString(R.string.pfk_dir_music), "") ?: ""
        val timeString = preferences.getString(getString(R.string.pfk_display_time), "1")
        val prefTime = timeString?.toIntOrNull() ?: 1
        val lblTime: TextClock = findViewById(R.id.lbl_time)
        lblMsg = findViewById(R.id.err_msg)

        isRandomTransition = prefTransition == Enums.PrefTransition.RANDOM.id
        if(!isRandomTransition) {
            animLst = getAnimationList(prefTransition)
        }

        when (prefTime) {
            Enums.PrefDisplayTime.TOP_END.id -> {
                lblTime.gravity = Gravity.END or Gravity.TOP
                lblTime.visibility = View.VISIBLE
            }
            Enums.PrefDisplayTime.TOP_START.id -> {
                lblTime.gravity = Gravity.START or Gravity.TOP
                lblTime.visibility = View.VISIBLE
            }
            Enums.PrefDisplayTime.BOTTOM_END.id -> {
                lblTime.gravity = Gravity.END or Gravity.BOTTOM
                lblTime.visibility = View.VISIBLE
            }
            Enums.PrefDisplayTime.BOTTOM_START.id -> {
                lblTime.gravity = Gravity.START or Gravity.BOTTOM
                lblTime.visibility = View.VISIBLE
            }
            else -> {
                lblTime.visibility = View.GONE
            }
        }

        val gestureDetector = GestureDetector(this, object: GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
                return true
            }
            override fun onSingleTapUp(event: MotionEvent): Boolean {
                if (isSysBarsVisible) {
                    hideSystemBarsWithDelay(true)
                } else {
                    showHideSystemBarsWithDelay()
                }
                return true
            }
//            override fun onLongPress(event: MotionEvent) {
//                Log.d(Constant.TAG, "onLongPress: $event")
//            }
            override fun onDoubleTap(event: MotionEvent): Boolean {
                if (!disableInputs) {
                    val isPrev = when {
                        event.rawX < (screenX / 2) -> { true }
                        else -> { false }
                    }
                    showMedia(isPrev)
                }
                return true
            }
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (!disableInputs) {
                    try {
                        e1?.let {
                            val diffY = e2.y - it.y
                            val diffX = e2.x - it.x

                            if (abs(diffX) > abs(diffY) && abs(diffX) > Constant.SWIPE_THRESHOLD && abs(velocityX) > Constant.SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX <= 0) {
                                    showMedia()
                                } else {
                                    showMedia(true)
                                }
                            } else if (abs(diffY) > abs(diffX) && abs(diffY) > Constant.SWIPE_THRESHOLD && abs(velocityY) > Constant.SWIPE_VELOCITY_THRESHOLD) {
                                if (!hasMediaAudio) {
                                    if (diffY > 0) {
                                        musicService?.next(true)
                                    } else {
                                        musicService?.previous(true)
                                    }
                                }
                                return true
                            }
                        } ?: run {
                            // Handle the case where e1 is null if needed
                        }
                    }
                    catch (exception: Exception) {
                        //exception.printStackTrace()
                    }
                }
                return true
            }
        })

        val container = findViewById<CoordinatorLayout>(R.id.container_slideshow)
        container.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        container.setOnKeyListener { _, i, keyEvent ->
            if (!disableInputs && keyEvent.action == KeyEvent.ACTION_UP) {
                when (i) {
//                    KeyEvent.KEYCODE_DPAD_CENTER -> {
//                        if (isSysBarsVisible) {
//                            hideSystemBarsWithDelay()
//                        } else {
//                            showSystemBars()
//                            hideSystemBarsWithDelay()
//                        }
//                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        showMedia()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        showMedia(true)
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!hasMediaAudio) {
                            musicService?.previous(true)
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        if (!hasMediaAudio) {
                            musicService?.next(true)
                        }
                    }
                }
            }
            false
        }

        textureView = findViewById(R.id.texture_view)
        imageView = findViewById(R.id.image_view)
        imageView.scaleType = when (prefScale) {
            Enums.PrefMediaFit.COVER.id -> { ImageView.ScaleType.CENTER_CROP }
            Enums.PrefMediaFit.FILL.id -> { ImageView.ScaleType.FIT_XY }
            else -> { ImageView.ScaleType.FIT_CENTER }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (adService.isInsideAd()) {
                    adService.closeAd()
                    return
                } else {
                    finish()
                }
            }
        })

        mediaInit()
    }

    private fun pauseMedia() {
        timerImage?.cancel()

        if (mediaService != null) {
            mediaService?.reset()
        }

        if (musicService != null) {
            musicService?.pause()
        }
    }

    private fun getAnimationList(prefTransition: Int): Map<String, Int> {
        return when (prefTransition) {
            Enums.PrefTransition.SLIDE.id -> mapOf(
                "animNextOut" to R.anim.slide_next_out,
                "animNextIn" to R.anim.slide_next,
                "animPrevOut" to R.anim.slide_prev_out,
                "animPrevIn" to R.anim.slide_prev
            )
            Enums.PrefTransition.SCROLL.id -> mapOf(
                "animNextOut" to R.anim.scroll_next_out,
                "animNextIn" to R.anim.scroll_next,
                "animPrevOut" to R.anim.scroll_prev_out,
                "animPrevIn" to R.anim.scroll_prev
            )
            Enums.PrefTransition.SCALE.id -> mapOf(
                "animNextOut" to R.anim.scale_out,
                "animNextIn" to R.anim.scale,
                "animPrevOut" to R.anim.scale_in,
                "animPrevIn" to R.anim.scale
            )
            Enums.PrefTransition.ROTATE.id -> mapOf(
                "animNextOut" to R.anim.rotate_next_out,
                "animNextIn" to R.anim.rotate_next,
                "animPrevOut" to R.anim.rotate_prev_out,
                "animPrevIn" to R.anim.rotate_prev
            )
            else -> mapOf(
                "animNextOut" to R.anim.fade_out,
                "animNextIn" to R.anim.fade_in,
                "animPrevOut" to R.anim.fade_out,
                "animPrevIn" to R.anim.fade_in
            )
        }
    }

    private fun showMedia(isPrev: Boolean = false) {
        disableInputs = true
        timerImage?.cancel()
//        mediaService?.reset()

        if (adService.isAdReady()) {
            adService.showAd()
            return
        }

        mediaIx = if (isPrev) mediaIx - 1 else mediaIx + 1
        if (mediaIx >= mediaLst.size) mediaIx = 0 else if (mediaIx < 0) mediaIx = mediaLst.size - 1

        val uri: Uri = Uri.parse(mediaLst[mediaIx])
        val mimeType: String = URLConnection.guessContentTypeFromName(uri.path)
        if (mimeType.startsWith("image")) {
            swapMedia(Enums.MediaType.IMAGE, uri, isPrev)
        } else if (mimeType.startsWith("video")) {
            swapMedia(Enums.MediaType.VIDEO, uri, isPrev)
        }
    }

    private fun swapMedia(curContentType: Enums.MediaType, uri: Uri, isPrev: Boolean) {
        val setAnimOut: Int
        val setAnimIn: Int

        if (isRandomTransition) {
            val rndNum = (3..6).random()
            animLst = getAnimationList(rndNum)
        }

        if (isPrev) {
            setAnimOut = animLst["animPrevOut"]!!
            setAnimIn = animLst["animPrevIn"]!!
        } else {
            setAnimOut = animLst["animNextOut"]!!
            setAnimIn = animLst["animNextIn"]!!
        }

        //if (curContentType == Enums.MediaType.VIDEO || prevContentType == Enums.MediaType.VIDEO) {
        //    setAnimOut = R.anim.fade_out
        //}

        val animOut: Animation = AnimationUtils.loadAnimation(this, setAnimOut).apply { startOffset = 2 }
        animIn = AnimationUtils.loadAnimation(this, setAnimIn).apply { startOffset = 2 }

        when {
            curContentType == Enums.MediaType.VIDEO && prevContentType == Enums.MediaType.VIDEO -> textureView.startAnimation(animOut)
            curContentType == Enums.MediaType.VIDEO && prevContentType == Enums.MediaType.IMAGE -> imageView.startAnimation(animOut)
            curContentType == Enums.MediaType.IMAGE && prevContentType == Enums.MediaType.VIDEO -> textureView.startAnimation(animOut)
            else -> imageView.startAnimation(animOut)
        }

//        if (mediaService != null) {
//            mediaService?.stop()
//        }

        if (curContentType == Enums.MediaType.IMAGE) {
            handleImageMedia(uri, animOut)
        } else if (curContentType == Enums.MediaType.VIDEO) {
            handleVideoMedia(uri, animOut)
        }
    }

    @Synchronized
    private fun handleImageMedia(uri: Uri, animOut: Animation) {
        runOnUiThread {
            prevContentType = Enums.MediaType.IMAGE
            hasMediaAudio = false
            musicService?.resume()

            animOut.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    imageView.visibility = View.INVISIBLE
                    textureView.visibility = View.INVISIBLE
                    textureView.alpha = 0F
                    mediaService?.reset()
                    glide.clear(imageView)
                    glide.load(uri.path)
                        .listener(object: RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                disableInputs = false
                                return false
                            }
                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    imageView.startAnimation(animIn)
                                    imageView.visibility = View.VISIBLE
                                    animIn.setAnimationListener(object :
                                        Animation.AnimationListener {
                                        override fun onAnimationRepeat(animation: Animation) {}
                                        override fun onAnimationStart(animation: Animation) {}
                                        override fun onAnimationEnd(animation: Animation) {
                                            startImageTimer()
                                            disableInputs = false
                                        }
                                    })
                                }, 150)
                                return false
                            }
                        })
                        .into(imageView)
                }
            })
        }
    }

    @Synchronized
    private fun handleVideoMedia(uri: Uri, animOut: Animation) {
        runOnUiThread {
            prevContentType = Enums.MediaType.VIDEO
            uri.path?.let { path ->
                hasMediaAudio = mediaService?.hasAudioTrack(path) ?: false
            } ?: run {
                hasMediaAudio = false
            }
            if (hasMediaAudio) {
                musicService?.pause()
            } else {
                musicService?.resume()
            }

            animOut.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    imageView.visibility = View.INVISIBLE
                    textureView.visibility = View.INVISIBLE
                    textureView.alpha = 0F
                    mediaService?.reset()
                    mediaService?.prepare(uri.path!!)
                    mediaService?.onVideoPreparedListener = this@SlideShowActivity
                }
            })
        }
    }

    override fun onVideoPrepared() {
        runOnUiThread {
            adjustAspectRatio()
            mediaService?.play()
            Handler(Looper.getMainLooper()).postDelayed({
                textureView.startAnimation(animIn)
            }, 250)
            animIn.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationStart(animation: Animation) {
                    textureView.alpha = 1F
                    textureView.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animation) {
                    disableInputs = false
                }
            })
        }
    }

    private fun adjustAspectRatio() {
        runOnUiThread {
            mediaService?.videoPlayer?.let { mediaPlayer ->
                val videoWidth = mediaPlayer.videoWidth
                val videoHeight = mediaPlayer.videoHeight
                val aspectRatio: Float = videoWidth / videoHeight.toFloat()
                val screenRatio = screenX / screenY.toFloat()
                val scaleX = aspectRatio / screenRatio
                when (prefScale) {
                    Enums.PrefMediaFit.COVER.id -> {
                        if (scaleX >= 1f) {
                            textureView.scaleX = scaleX
                        } else {
                            textureView.scaleY = 1f / scaleX
                        }
                    }

                    Enums.PrefMediaFit.FILL.id -> {
                        textureView.scaleX = 1f
                        textureView.scaleY = 1f
                    }

                    else -> {
                        if (scaleX >= 1f) {
                            textureView.scaleY = 1f / scaleX
                        } else {
                            textureView.scaleX = scaleX
                        }
                    }
                }
            }
        }
    }

    fun onSurfaceTextureAvailableCallback() {
        musicInit()
        showMedia()
    }

    fun onSurfaceTextureDestroyedCallback() {
        try {
            adService.release()
            mediaService?.release()
            mediaService = null
            musicService?.release()
            musicService = null
        } catch (_: Exception) { }
    }

    fun videoOnCompletionCallback() {
        showMedia()
    }

    private fun startImageTimer() {
        timerImage = object: CountDownTimer(prefDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                showMedia()
            }
        }
        timerImage?.start()
    }

    private fun mediaInit() {
        val mediaList = ArrayList<Uri>()
        val savedDirs = ArrayList(prefDirMedia.split(Constant.FOLDER_DELIMITER))
        val validDirs = Utils.getPathsFromBucketIds(this, savedDirs) as ArrayList<FolderVm>

        validDirs.forEach { dir ->
            File(dir.path).walkTopDown().maxDepth(1).forEach { file ->
                if (!file.absolutePath.contains("/.")) {
                    val mimeType = URLConnection.guessContentTypeFromName(file.absolutePath)
                    if (mimeType != null && mimeType !in listOf("image/gif", "image/giff")) {
                        when {
                            prefMediaType == Enums.PrefMediaType.All.id && (mimeType.startsWith("image") || mimeType.startsWith("video")) -> {
                                mediaList.add(file.toUri())
                                mediaLst.add(file.absolutePath)
                            }
                            prefMediaType == Enums.PrefMediaType.IMAGES.id && mimeType.startsWith("image") -> {
                                mediaList.add(file.toUri())
                                mediaLst.add(file.absolutePath)
                            }
                            prefMediaType == Enums.PrefMediaType.VIDEOS.id && mimeType.startsWith("video") -> {
                                mediaList.add(file.toUri())
                                mediaLst.add(file.absolutePath)
                            }
                        }
                    }
                }
            }
        }

        if (mediaList.isNotEmpty()) {
            lblMsg.visibility = View.GONE
            mediaService = MediaService(this, textureView)
            mediaService?.onVideoPreparedListener = this
        } else {
            disableInputs = true
            textureView.visibility = View.INVISIBLE
            imageView.visibility = View.INVISIBLE
            lblMsg.visibility = View.VISIBLE
        }
    }

    private fun musicInit() {
        val songList = ArrayList<Uri>()
        val savedDirs = ArrayList(prefDirMusic.split(Constant.FOLDER_DELIMITER))
        val validDirs = Utils.getPathsFromPaths(savedDirs) as ArrayList<FolderVm>
        val supportedExtensionsSet = setOf("mp3", "aac", "m4a", "wav", "flac", "ogg", "wma", "alac").map { it.lowercase() }.toSet()

        validDirs.forEach { dir ->
            File(dir.path).walkTopDown().maxDepth(1).forEach { file ->
                if (!file.absolutePath.contains("/.")) {
                    val fileExtension = file.extension.lowercase()
                    if (fileExtension in supportedExtensionsSet) {
                        songList.add(file.toUri())
                    }
                }
            }
        }

        if (songList.isNotEmpty()) {
            musicService = MusicService(this, songList)
            musicService?.play()
        }
    }

    private fun hideSystemBars() {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                winInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            }
            isSysBarsVisible = false
        }
    }

    private fun showSystemBars() {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                winInsetsController?.show(WindowInsetsCompat.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        )
                //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
            isSysBarsVisible = true
        }
    }

    private fun showHideSystemBarsWithDelay() {
        runOnUiThread {
            showSystemBars()
            hideSystemBarsWithDelay()
        }
    }

    private fun hideSystemBarsWithDelay(forceHide: Boolean = false) {
        val delayInMillis: Long = if (forceHide) 20 else 2550
        sysBarHandler.removeCallbacks(hideSystemBarsRunnable)
        sysBarHandler.postDelayed(hideSystemBarsRunnable, delayInMillis)
    }

    private fun showHelpDialog() {
        val richText = getString(R.string.slideshow_help)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.slideshow_help)
        val body = dialog.findViewById<TextView>(R.id.tv_help)
        body.text =  HtmlCompat.fromHtml(richText, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Handle landscape orientation
            // For example, show a full-screen video player
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Handle portrait orientation
            // For example, restore the original video player UI
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        timerImage?.cancel()
        adService.release()
        mediaService?.release()
        mediaService = null
        musicService?.release()
        musicService = null
        sysBarHandler.removeCallbacks(hideSystemBarsRunnable)
   }
}
