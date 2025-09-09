package com.aiapp.flowcent.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        val pagerState = rememberPagerState(pageCount = { state.totalPages })
        val scope = rememberCoroutineScope()

        // Keep VM in sync with page changes
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.onAction(UserAction.GoTo(page))
            }
        }

        // Animated gradient background that shifts by page
        val (startTarget, endTarget) = when (pagerState.currentPage) {
            0 -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
            1 -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.secondary
        }
        val startColor by animateColorAsState(targetValue = startTarget, animationSpec = tween(500, easing = FastOutSlowInEasing), label = "bgStart")
        val endColor by animateColorAsState(targetValue = endTarget, animationSpec = tween(500, easing = FastOutSlowInEasing), label = "bgEnd")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(listOf(startColor, endColor))
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            // Skip at top-right
            Text(
                text = "Skip",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable(enabled = !state.isSaving) {
                        viewModel.onAction(UserAction.Skip)
                        onFinished()
                    }
            )

            // Centered content and button
            val interaction = remember { MutableInteractionSource() }
            val pressed by interaction.collectIsPressedAsState()
            val scale = if (pressed) 0.98f else 1f

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HorizontalPager(state = pagerState, userScrollEnabled = true) { page ->
                    AnimatedContent(
                        targetState = page,
                        transitionSpec = {
                            (slideInVertically(
                                initialOffsetY = { it / 8 },
                                animationSpec = tween(450, easing = FastOutSlowInEasing)
                            ) + fadeIn()) togetherWith (slideOutVertically(
                                targetOffsetY = { -it / 8 },
                                animationSpec = tween(350, easing = FastOutSlowInEasing)
                            ) + fadeOut())
                        }, label = "pageTransition"
                    ) { target ->
                        when (target) {
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
                    }
                }

                DotsIndicator(
                    total = state.totalPages,
                    selectedIndex = pagerState.currentPage,
                    onDotClick = { _ -> }
                )

                PrimaryGradientButton(
                    text = if (pagerState.currentPage >= state.totalPages - 1) "Get started" else "Next",
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(scale),
                    brush = Brush.horizontalGradient(colors = listOf(endColor, startColor)),
                    interaction = interaction
                ) {
                    if (pagerState.currentPage >= state.totalPages - 1) {
                        viewModel.onAction(UserAction.Complete)
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = caption,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)),
                    shape = MaterialTheme.shapes.medium
                )
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Illustration",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun DotsIndicator(total: Int, selectedIndex: Int, onDotClick: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { index ->
            val selected = index == selectedIndex
            val width = if (selected) 24.dp else 8.dp
            val color = if (selected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.95f)
            else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.45f)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
                    .height(8.dp)
                    .width(width)
            )
        }
    }
}

// Center illustration removed per request


@Composable
private fun PrimaryGradientButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    brush: Brush,
    interaction: MutableInteractionSource,
    onClick: () -> Unit
) {
    val shape = MaterialTheme.shapes.large
    val contentColor = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
            .clickable(enabled = enabled, interactionSource = interaction, indication = null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = contentColor)
    }
}
