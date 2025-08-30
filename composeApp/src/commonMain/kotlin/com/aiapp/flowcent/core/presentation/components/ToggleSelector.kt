package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> ToggleSelector(
    options: List<Pair<T, String>>,        // Generic list of options (value + label)
    selectedOption: T,                     // Currently selected value
    onOptionSelected: (T) -> Unit,         // Callback when selection changes
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = Color.Transparent,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = RoundedCornerShape(0),
    paddingVertical: Dp = 16.dp,
    paddingHorizontal: Dp = 16.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = shape)
            .padding(horizontal = paddingHorizontal, paddingVertical),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { (value, label) ->
            val isSelected = selectedOption == value

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) selectedColor else unselectedColor)
                    .clickable { onOptionSelected(value) }
                    .padding(vertical = paddingVertical),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}


