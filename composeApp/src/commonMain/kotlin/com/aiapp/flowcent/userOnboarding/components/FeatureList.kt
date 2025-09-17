package com.aiapp.flowcent.userOnboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.userOnboarding.screens.FeatureItem

@Composable
fun FeatureList(features: List<FeatureItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(features) { index, feature ->
            AnimatedFeatureCard(feature, index)
        }
    }
}