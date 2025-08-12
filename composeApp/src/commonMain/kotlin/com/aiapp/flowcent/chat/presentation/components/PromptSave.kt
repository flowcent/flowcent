package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.core.presentation.components.AppCustomButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PromptSave(
    selectionType: AccountSelectionType = AccountSelectionType.PERSONAL,
    selectedAccountName: String = "",
    onClickSave: () -> Unit = {},
    onClickDiscard: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppCustomButton(
            onClick = { onClickSave() },
            btnText = if (selectionType == AccountSelectionType.SHARED) "Update $selectedAccountName" else "Save Into Personal",
            bgColor = Color.Black,
            textColor = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppCustomButton(
            onClick = { onClickDiscard() },
            btnText = "Discard",
            bgColor = Color.Black,
            textColor = Color.White
        )
    }
}