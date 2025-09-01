package com.aiapp.flowcent.subscription.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
fun RcPaywall(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
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