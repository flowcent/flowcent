import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.components.AppOutlineButton

@Composable
fun ChatOnBoardTypingText(
    onClickText: (text: String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Chat naturally about your daily \n" +
                    "expenses and let AI automatically \n" +
                    "categorize and track your spending.",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Type some prompts like these:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppOutlineButton(
            text = "I bought a shirt with 49 dollar",
            onClick = {
                onClickText("I bought a shirt with 49 dollar")
            },
            hasGradient = false,
            outlineColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
            cornerRadius = 50
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppOutlineButton(
            text = "Just had my dinner with wifey that cost 90 dollar",
            onClick = {
                onClickText("Just had my dinner with wifey that cost 90 dollar")
            },
            hasGradient = false,
            outlineColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
            cornerRadius = 50
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppOutlineButton(
            text = "House rent $2000, shopping $350, transport $80",
            onClick = {
                onClickText("House rent $2000, shopping $350, transport $80")
            },
            hasGradient = false,
            outlineColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
            cornerRadius = 50
        )
    }

}
