import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ChatOnBoardTypingText(
    typingSpeed: Long = 50L, // ms per character
    pauseBetween: Long = 500L // pause before next line
) {
    var visibleText by remember { mutableStateOf("") }

    val texts = listOf(
        "Chat naturally about your daily \nexpenses and let AI automatically \ncategorize and track your spending.",
        "Type some prompts like these:",
        "• I bought a shirt with 49 dollar",
        "• Just had my dinner with wifey that cost 90 dollar",
        "• House rent \$2000, shopping \$350, transport \$80"
    )


    LaunchedEffect(Unit) {
        visibleText = ""
        for (line in texts) {
            for (i in line.indices) {
                visibleText += line[i]
                delay(typingSpeed)
            }
            visibleText += "\n" // move to next line
            visibleText += "\n" // move to next line
            delay(pauseBetween)
        }
    }

    Text(
        text = visibleText,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Unspecified
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                val gradient = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF5769F2),
                        Color(0xFF8442EC),
                        Color(0xFF9036E9)
                    )
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcAtop)
                }
            }
    )
}
