package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomBalanceSlider(
    openingBalance: Double = 0.0,
    onSliderValueChange: (amount: Double) -> Unit
) {
    val steps = 10
    var sliderPosition by remember { mutableStateOf((steps / 2).toFloat()) }
    val segmentValue = openingBalance / steps
    val selectedValue = (sliderPosition * segmentValue.toInt()).toInt()

    val lineColor = MaterialTheme.colorScheme.surfaceVariant

    fun handleSliderPosChange(position: Float) {
        sliderPosition = position
        onSliderValueChange(selectedValue.toDouble())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Big Number
        Text(
            text = "$selectedValue $",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Custom Track with Tick Marks
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(40.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val tickCount = steps
                val spacing = size.width / (tickCount - 1)

                // Draw ticks
                for (i in 0 until tickCount) {
                    val x = i * spacing
                    val tickHeight =
                        if (i == (sliderPosition - 1).toInt()) 20.dp.toPx() else 12.dp.toPx()
                    drawLine(
                        color = lineColor,
                        start = Offset(x, size.height / 2 - tickHeight / 2),
                        end = Offset(x, size.height / 2 + tickHeight / 2),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            // Actual Slider over the ticks
            Slider(
                value = sliderPosition,
                onValueChange = { handleSliderPosChange(it) },
                valueRange = 1f..steps.toFloat(),
                steps = steps - 2,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderColors(
                    thumbColor = MaterialTheme.colorScheme.onSurface,
                    activeTrackColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Transparent,
                    disabledActiveTickColor = Color.Transparent,
                    disabledInactiveTrackColor = Color.Transparent,
                    disabledInactiveTickColor = Color.Transparent
                )
            )
        }
    }
}
