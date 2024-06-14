package com.lvalentin.animaroll.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.billingclient.api.ProductDetails
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.lvalentin.animaroll.R
import com.lvalentin.animaroll.common.Constant


class AdService(private val context: Context, private val adLayout: View, private val isTelevision: Boolean): BillingUpdatesListener {
    private var currentNativeAd: NativeAd? = null
    private var adListener: AdEventListener? = null
    private var tvAdAutoClose: TextView? = null
    private var timerAd: CountDownTimer? = null
    private var timerAdAutoClose: CountDownTimer? = null
    private var isAdReady: Boolean = false
    private var isInsideAd: Boolean = false
    private var isAdTimerActive: Boolean = false
    private val adInterval: Long = Constant.AD_TIMER
    private val adAutoCloseDuration: Long = Constant.AD_AUTOCLOSE_TIMER

//    private val billingService: BillingService = BillingService(context, this)
    private var noAdsProductDetails: ProductDetails? = null

    interface AdEventListener {
        fun onAdLoaded()
        fun onAdFailedToLoad(error: LoadAdError)
        fun onAdClicked()
        fun onAdClosed()
        fun onAddVideoEnd()
    }

    fun setAdListener(listener: AdEventListener) {
        adListener = listener
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun scheduleAds() {
        if (isAdTimerActive) {
            return
        }

        isAdTimerActive = true

        timerAd?.cancel()
        timerAd = object: CountDownTimer(adInterval, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Optionally update UI with the countdown for the next ad
            }
            override fun onFinish() {
                isAdReady = true
                isAdTimerActive = false
            }
        }
        timerAd?.start()
    }

    private fun loadAd() {
        if (!isNetworkAvailable(context) || isTelevision) {
            displayOfflinePlaceholder()
            return
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        val adLoader = AdLoader.Builder(context, context.getString(R.string.admob_native_unit_id))
            .forNativeAd { nativeAd: NativeAd ->
                if (currentNativeAd != null) {
                    currentNativeAd?.destroy()
                }

                isAdReady = false
                isInsideAd = true
                currentNativeAd = nativeAd

                val inflater = LayoutInflater.from(context)
                val adView = inflater.inflate(R.layout.ad_activity, adLayout as ViewGroup,false) as LinearLayout
                val nativeAdView = adView.findViewById<NativeAdView>(R.id.ad_online_placeholder)
                val offlineAdView = adView.findViewById<RelativeLayout>(R.id.ad_offline_placeholder)

                populateNativeAdView(nativeAd, nativeAdView)

                adLayout.removeAllViews()
                adLayout.addView(adView)
                adLayout.invalidate()

                nativeAdView.visibility = View.VISIBLE
                offlineAdView.visibility = View.GONE
                adLayout.visibility = View.VISIBLE

                val btnAdClose = adView.findViewById<Button>(R.id.ad_close)
                btnAdClose.visibility = when (isTelevision) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }
                btnAdClose.setOnClickListener {
                    closeAd()
                }

                tvAdAutoClose = adView.findViewById(R.id.ad_autoclose)
                tvAdAutoClose?.visibility = View.VISIBLE

                timerAdAutoClose?.cancel()
                timerAdAutoClose = object: CountDownTimer(adAutoCloseDuration, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvAdAutoClose?.text = context.getString(R.string.ad_closing, millisUntilFinished / 1000)
                    }
                    override fun onFinish() {
                        if (isInsideAd) {
                            closeAd()
                        }
                    }
                }
                timerAdAutoClose?.start()

                val mediaContent = nativeAd.mediaContent
                val vc = mediaContent?.videoController
                if (vc != null && mediaContent.hasVideoContent()) {
                    val mediaAspectRatio: Float? = nativeAd.mediaContent?.aspectRatio
                    val duration: Float? = nativeAd.mediaContent?.duration
                    Log.d(Constant.TAG, "mediaAspectRatio: $mediaAspectRatio")
                    Log.d(Constant.TAG, "duration: $duration")

                    vc.videoLifecycleCallbacks =
                        object: VideoController.VideoLifecycleCallbacks() {
                            override fun onVideoEnd() {
                                super.onVideoEnd()
                                isAdTimerActive = false
                                isInsideAd = false
                                adListener?.onAddVideoEnd()
                            }
                        }
                }
            }
            .withAdListener(object: AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    isInsideAd = true
                    isAdTimerActive = false
                    adListener?.onAdLoaded()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adListener?.onAdFailedToLoad(adError)
                    displayOfflinePlaceholder()
                    return
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    tvAdAutoClose?.visibility = View.GONE
                    timerAdAutoClose?.cancel()
                    adListener?.onAdClicked()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    closeAd()
                }
            })
            .withNativeAdOptions(adOptions)
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onProductDetailsUpdated(productDetails: ProductDetails) {
        if (productDetails.productId == "test") {
            noAdsProductDetails = productDetails
        }
    }

    override fun onPurchaseStateChanged() {
        TODO("Not yet implemented")
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView?)?.text = nativeAd.headline
        adView.mediaView?.mediaContent = nativeAd.mediaContent
        adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

        if (nativeAd.body != null) {
            (adView.bodyView as TextView?)?.text = nativeAd.body
            adView.bodyView?.visibility = View.VISIBLE
        } else {
            adView.bodyView?.visibility = View.GONE
        }

        if (nativeAd.callToAction != null) {
            (adView.callToActionView as Button?)?.text = nativeAd.callToAction
            adView.callToActionView?.visibility = when (isTelevision) {
                true -> View.GONE
                false -> View.VISIBLE
            }
        } else {
            adView.callToActionView?.visibility = View.GONE
        }

        if (nativeAd.icon != null) {
            (adView.iconView as ImageView?)?.setImageDrawable(nativeAd.icon!!.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } else {
            adView.iconView?.visibility = View.GONE
        }

        if (nativeAd.price != null) {
            (adView.priceView as TextView?)?.text = nativeAd.price
            adView.priceView?.visibility = View.VISIBLE
        } else {
            adView.priceView?.visibility = View.GONE
        }

        if (nativeAd.starRating != null) {
            (adView.starRatingView as RatingBar?)?.rating = nativeAd.starRating?.toFloat()!!
            adView.starRatingView?.visibility = View.VISIBLE
        } else {
            adView.starRatingView?.visibility = View.GONE
        }

        adView.storeView?.visibility = View.GONE
        adView.advertiserView?.visibility = View.GONE
        if (nativeAd.store != null) {
            (adView.storeView as TextView?)?.text = nativeAd.store
            adView.storeView?.visibility = View.VISIBLE
        } else if (nativeAd.advertiser != null) {
            (adView.advertiserView as TextView?)?.text = nativeAd.advertiser
            adView.advertiserView?.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }

    private fun displayOfflinePlaceholder() {
        isAdReady = false
        isInsideAd = true
        isAdTimerActive = false
        currentNativeAd?.destroy()
        currentNativeAd = null
        adListener?.onAdLoaded()

        val inflater = LayoutInflater.from(context)
        val adView = inflater.inflate(R.layout.ad_activity, adLayout as ViewGroup,false) as LinearLayout
        val nativeAdView = adView.findViewById<NativeAdView>(R.id.ad_online_placeholder)
        val offlineAdView = adView.findViewById<RelativeLayout>(R.id.ad_offline_placeholder)

        adLayout.removeAllViews()
        adLayout.addView(adView)
        adLayout.invalidate()

        val btnAdClose = adView.findViewById<Button>(R.id.ad_close)
        btnAdClose.visibility = when (isTelevision) {
            true -> View.GONE
            false -> View.VISIBLE
        }
        btnAdClose.setOnClickListener {
            closeAd()
        }

//        val upgradeButton = adView.findViewById<Button>(R.id.upgrade_button)
//        upgradeButton.visibility = when (isTelevision) {
//            true -> View.GONE
//            false -> View.VISIBLE
//        }
//        upgradeButton.setOnClickListener {
//            noAdsProductDetails?.let {
//                if (context is Activity) {
//                    adListener?.onAdClicked()
//                    timerAdAutoClose?.cancel()
//                    tvAdAutoClose?.visibility = View.GONE
////                    billingService.launchPurchaseFlow(context, it)
//                }
//            }
//        }

        tvAdAutoClose = adView.findViewById(R.id.ad_autoclose)
        tvAdAutoClose?.visibility = View.VISIBLE

        timerAdAutoClose?.cancel()
        timerAdAutoClose = object: CountDownTimer(adAutoCloseDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvAdAutoClose?.text = context.getString(R.string.ad_closing, millisUntilFinished / 1000)
            }
            override fun onFinish() {
                if (isInsideAd) {
                    closeAd()
                }
            }
        }
        timerAdAutoClose?.start()

        nativeAdView.visibility = View.GONE
        offlineAdView.visibility = View.VISIBLE
        adLayout.visibility = View.VISIBLE
    }

    fun initAd() {
        scheduleAds()
    }

    fun showAd() {
        if (!isInsideAd) {
            loadAd()
        }
    }

    fun isAdReady(): Boolean {
        return isAdReady && !isInsideAd
    }

    fun isInsideAd(): Boolean {
        return isInsideAd
    }

    fun closeAd() {
        isInsideAd = false
        isAdTimerActive = false
        timerAdAutoClose?.cancel()
        currentNativeAd?.destroy()
        currentNativeAd = null
        (adLayout as ViewGroup).removeAllViews()
        adLayout.visibility = View.GONE
        adListener?.onAdClosed()
        scheduleAds()
    }

    fun release() {
        timerAd?.cancel()
        timerAdAutoClose?.cancel()
        currentNativeAd?.destroy()
        currentNativeAd = null
    }
}