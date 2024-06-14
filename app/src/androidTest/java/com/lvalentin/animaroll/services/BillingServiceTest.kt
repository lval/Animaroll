package com.lvalentin.animaroll.services

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BillingServiceTest {

    private lateinit var context: Context
    private lateinit var billingService: BillingService
    private lateinit var billingClient: BillingClient

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        billingService = BillingService(context, object: BillingUpdatesListener {
            override fun onProductDetailsUpdated(productDetails: ProductDetails) {
                //
            }
            override fun onPurchaseStateChanged() {
                //
            }
        })

        billingClient = BillingClient.newBuilder(context)
            .setListener(billingService)
            .enablePendingPurchases()
            .build()
    }

    @Test
    fun testBillingClientSetup() {
        // Your test implementation
    }
}