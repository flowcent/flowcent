package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingBottomSection(
    pagerState: PagerState,
    hasSkip: Boolean = false,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (hasSkip) Arrangement.SpaceBetween else Arrangement.End
    ) {
        if (hasSkip) {
            Text(
                text = "Skip",
                modifier = Modifier.clickable(onClick = onSkipClicked),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        CircularProgressButton(
            progress = (pagerState.currentPage + 1) / pagerState.pageCount.toFloat(),
            onClick = onNextClicked
        )
    }
}