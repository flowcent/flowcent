package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.core.presentation.components.AppOutlineButton
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PromptSave(
    selectionType: AccountSelectionType = AccountSelectionType.PERSONAL,
    selectedAccountName: String = "",
    isLoadingSave: Boolean = false,
    isLoadingDiscard: Boolean = false,
    onClickSave: () -> Unit = {},
    onClickDiscard: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppOutlineButton(
            text = if (selectionType == AccountSelectionType.PERSONAL) {
                if (isLoadingSave) "Saving..." else "Save Into Personal"
            } else {
                if (isLoadingSave) "Updating..." else "Update Accounts"
            },
            onClick = { onClickSave() },
            hasGradient = false,
            outlineColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Default.Save,
            iconColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
            isLoading = isLoadingSave
        )

        AppOutlineButton(
            text = if (isLoadingDiscard) "Discarding..." else "Discard",
            onClick = { onClickDiscard() },
            hasGradient = false,
            outlineColor = Colors.ExpenseColor,
            icon = Icons.Default.Close,
            iconColor = Colors.ExpenseColor,
            textColor = Colors.ExpenseColor,
            isLoading = isLoadingDiscard
        )

    }
}