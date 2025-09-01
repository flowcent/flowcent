package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.core.presentation.components.AppCustomButton
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PromptSave(
    selectionType: AccountSelectionType = AccountSelectionType.PERSONAL,
    selectedAccountName: String = "",
    onClickSave: () -> Unit = {},
    onClickDiscard: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppCustomButton(
            btnText = "Save",
            onClick = { onClickSave() },
            textColor = MaterialTheme.colorScheme.onPrimary,
            bgColor = MaterialTheme.colorScheme.primary,
            shouldFillMaxWidth = false,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(16.dp))


        AppCustomButton(
            btnText = "Discard",
            onClick = { onClickDiscard() },
            textColor = Color.White,
            bgColor = Colors.Red300,
            shouldFillMaxWidth = false,
            modifier = Modifier.weight(1f)
        )
    }
}