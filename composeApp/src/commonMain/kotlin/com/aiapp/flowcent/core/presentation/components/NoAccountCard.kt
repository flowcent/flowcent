package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoAccountCard(
    onClick: () -> Unit
) {
    val features = listOf(
        "Track expenses in real time",
        "Create a custom budget & categories",
        "Manage recurring transactions"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Top Testimonial Section
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFF1DE))
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Stars
                    Text("★★★★★", color = Color(0xFFFFA500), fontSize = 16.sp)

                    // Placeholder for Pie Chart or Logo
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFFF8C00), shape = CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "“I created a personalized budget with custom categories, and it’s totally changed how I manage my money.”",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Text(
                    text = "Sonia H.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Bottom Feature Section
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Manage your spending.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                features.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("•", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(it, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    btnText = "CONNECT YOUR ACCOUNTS",
                    textColor = Color.White,
                    onClick = { onClick() },
                    style = ButtonStyle.PRIMARY,
                    bgColor = Color(0xFF000000)
                )
            }
        }
    }
}
