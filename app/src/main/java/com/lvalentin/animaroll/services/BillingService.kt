package com.lvalentin.animaroll.services

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.lvalentin.animaroll.R
import com.lvalentin.animaroll.common.Constant
import com.lvalentin.animaroll.common.PurchaseUtils
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

interface BillingUpdatesListener {
    fun onProductDetailsUpdated(productDetails: ProductDetails)
    fun onPurchaseStateChanged()
}

class BillingService(private val context: Context, private val listener: BillingUpdatesListener): PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null
    private val productNoAds: String = "test3"
    private val productList = listOf(
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productNoAds)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    )

    init {
        initBillingClient()
    }

    fun initBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
            .build()

        billingClient?.startConnection(object: BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                Log.d(Constant.TAG, "Billing Connection Code: ${billingResult.responseCode}")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkPurchases()
                    getProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(Constant.TAG, "Billing Service Disconnected")
                initBillingClient()
            }
        })
    }

    private fun checkPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchases.isNotEmpty()) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else {
                    updatePurchaseState(false)
                    Log.d(Constant.TAG,"No existing purchases")
                }
            }
        }
    }

    private fun getProducts() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient?.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                for (productDetail in productDetailsList) {
                    Log.d(Constant.TAG, "Product Found: ${productDetail.productId}")
                    if (productDetail.productId == productNoAds) {
                        listener.onProductDetailsUpdated(productDetail)
                    }
                }
            } else {
                Log.d(Constant.TAG, "ShowProducts error: " + billingResult.responseCode)
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        Log.d(Constant.TAG, "Purchase Flow: ${productDetails.productId} - ${productDetails.name}")

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient?.launchBillingFlow(activity, billingFlowParams)
        Log.d(Constant.TAG, "BillingFlow Response Code: ${billingResult?.responseCode}")
        if (billingResult?.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.d(Constant.TAG, "Error launching billing flow: ${billingResult?.debugMessage}")
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Log.d(Constant.TAG, "Product Purchased: ${purchase.products[0]}")
        if (productNoAds == purchase.products[0] && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (verifyPurchase(purchase.originalJson, purchase.signature)) {
                acknowledgePurchase(purchase)
            }
        } else if (productNoAds == purchase.products[0] && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Log.d(Constant.TAG, "Purchase is pending.")
        } else if (productNoAds == purchase.products[0] && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Log.d(Constant.TAG, "Purchase is unspecified.")
        }
    }

    private fun verifyPurchase(signedData: String, signature: String): Boolean {
        return try {
            val keyFactory = KeyFactory.getInstance("RSA")
            val keySpec = X509EncodedKeySpec(Base64.decode(context.getString(R.string.billing_public_key), Base64.DEFAULT))
            val publicKey = keyFactory.generatePublic(keySpec)
            val signatureInstance = Signature.getInstance("SHA1withRSA")
            signatureInstance.initVerify(publicKey)
            signatureInstance.update(signedData.toByteArray())
            signatureInstance.verify(Base64.decode(signature, Base64.DEFAULT))
        } catch (e: Exception) {
            false
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (purchase.isAcknowledged) {
            updatePurchaseState(true)
            Toast.makeText(context, "You are a premium user now", Toast.LENGTH_SHORT).show()
            Log.d(Constant.TAG, "Purchase is Acknowledge: ${purchase.products[0]}")
        } else {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
                if (productNoAds == purchase.products[0] && billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    updatePurchaseState(true)
                    Toast.makeText(context, "You are a premium user now", Toast.LENGTH_SHORT).show()
                    Log.d(Constant.TAG, "Purchase is Acknowledge: ${purchase.products[0]}")
                } else {
                    Log.d(Constant.TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        Log.d(Constant.TAG, "onPurchasesUpdated: ${billingResult.responseCode}")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            updatePurchaseState(true)
//            for (purchase in purchases) {
//                handlePurchase(purchase)
//            }
        }
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE) {
            Log.d(Constant.TAG, "Item unavailable")
            Toast.makeText(context, "Item is unavailable for purchase", Toast.LENGTH_SHORT).show()
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(Constant.TAG, "User cancelled purchase")
        } else {
            Log.d(Constant.TAG, "Purchase update failed: ${billingResult.debugMessage}")
        }
    }

    @Synchronized
    private fun updatePurchaseState(hasNoAds: Boolean) {
        PurchaseUtils.storePurchaseState(context, hasNoAds)
        listener.onPurchaseStateChanged()
    }

    fun endConnection() {
        billingClient?.endConnection()
    }
}