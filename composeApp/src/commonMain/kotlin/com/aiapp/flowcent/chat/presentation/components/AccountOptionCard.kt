package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.accounts.domain.model.AccountMember
import com.aiapp.flowcent.accounts.presentation.components.AvatarGroup
import com.aiapp.flowcent.core.presentation.components.NameInitial

@Composable
fun AccountOptionCard(
    members: List<AccountMember>,
    title: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(150.dp) // Square box
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF1A73E8) else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelected() }
            .padding(12.dp)
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top-left Radio Button
                RadioButton(
                    selected = selected,
                    onClick = onSelected,
                    modifier = Modifier.size(20.dp)
                        .align(Alignment.TopEnd)
                )
            }

            NameInitial(
                text = title
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarGroup(
                    members = members.map { it.memberLocalUserName },
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.width(80.dp)
                )
            }
        }
    }
}
