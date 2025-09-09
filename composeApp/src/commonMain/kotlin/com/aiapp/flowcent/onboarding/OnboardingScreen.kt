package com.aiapp.flowcent.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Content
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(24.dp))

                when (state.currentPage) {
                    0 -> OnboardPage(
                        title = "Add expenses by voice",
                        caption = "Just say it — we’ll parse and log it instantly.",
                    )
                    1 -> OnboardPage(
                        title = "Track group expenses",
                        caption = "Create groups, split bills, and keep everyone settled.",
                    )
                    else -> OnboardPage(
                        title = "Insights + chat",
                        caption = "Ask anything. Get summaries, trends, and smart nudges.",
                    )
                }

                Spacer(Modifier.height(16.dp))

                DotsIndicator(
                    total = state.totalPages,
                    selectedIndex = state.currentPage,
                    onDotClick = { viewModel.onAction(UserAction.GoTo(it)) }
                )
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skip",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable(enabled = !state.isSaving) {
                        viewModel.onAction(UserAction.Skip)
                        onFinished()
                    }
                )

                Button(
                    onClick = {
                        if (state.currentPage >= state.totalPages - 1) {
                            viewModel.onAction(UserAction.Complete)
                            onFinished()
                        } else {
                            viewModel.onAction(UserAction.Next)
                        }
                    },
                    enabled = !state.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(if (state.currentPage >= state.totalPages - 1) "Get started" else "Next")
                }
            }
        }
    }
}

@Composable
private fun OnboardPage(title: String, caption: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = caption,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        // Placeholder graphic block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Illustration",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun DotsIndicator(total: Int, selectedIndex: Int, onDotClick: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(total) { index ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .height(10.dp)
                    .padding(horizontal = 10.dp)
                    .clickable { onDotClick(index) }
            )
        }
    }
}

