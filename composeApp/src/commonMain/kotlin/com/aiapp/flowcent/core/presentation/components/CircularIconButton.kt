import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

sealed class CircularIcon {
    data class Vector(val imageVector: ImageVector) : CircularIcon()
    data class PainterIcon(val painter: Painter) : CircularIcon()
}

@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: CircularIcon = CircularIcon.Vector(Icons.Default.Send),
    containerColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    iconSize: Dp = 56.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconInnerSize: Dp = 28.dp
) {
    Box(
        modifier = modifier
            .size(iconSize)
            .clip(CircleShape)
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (icon) {
            is CircularIcon.Vector -> Icon(
                imageVector = icon.imageVector,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconInnerSize)
            )

            is CircularIcon.PainterIcon -> Icon(
                painter = icon.painter,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconInnerSize)
            )

            else -> {}
        }
    }
}
