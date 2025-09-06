package com.aiapp.flowcent.subscription.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesDelegate
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreProduct
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
fun RcPaywall(
    modifier: Modifier = Modifier,
    onUpdatePlan: (customerInfo: CustomerInfo) -> Unit,
    onDismiss: () -> Unit
) {

    Purchases.sharedInstance.delegate = object : PurchasesDelegate {
        override fun onCustomerInfoUpdated(customerInfo: CustomerInfo) {
            onUpdatePlan(customerInfo)
        }

        override fun onPurchasePromoProduct(
            product: StoreProduct,
            startPurchase: (onError: (error: PurchasesError, userCancelled: Boolean) -> Unit, onSuccess: (storeTransaction: StoreTransaction, customerInfo: CustomerInfo) -> Unit) -> Unit
        ) {

        }
    }

    val options = remember {
        PaywallOptions(dismissRequest = { onDismiss() })
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
    ) {
        com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall(options)
    }
}