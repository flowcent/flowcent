package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class ButtonStyle {
    PRIMARY, PRIMARY_COMPACT, SECONDARY, OUTLINED, APP_BAR_ACTION, SAMPLE_CARD
}


@Composable
fun AppCustomButton(
    modifier: Modifier = Modifier,
    btnText: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    iconRes: DrawableResource? = null,
    fontSize: TextUnit = 16.sp,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    shouldFillMaxWidth: Boolean = true,
    verticalPadding: Dp? = null,
    iconPadding: Dp = 8.dp,
    textColor: Color = Color.Black,
    bgColor: Color = Color.White,
    iconSize: Dp = 28.dp
) {
    val paddingHorizontal = when (style) {
        ButtonStyle.APP_BAR_ACTION -> 16.dp
        ButtonStyle.SAMPLE_CARD -> 40.dp
        else -> 24.dp
    }

    val paddingVertical = verticalPadding ?: when (style) {
        ButtonStyle.APP_BAR_ACTION -> 6.dp
        ButtonStyle.PRIMARY_COMPACT -> 10.dp
        else -> 12.dp
    }

    val shape = RoundedCornerShape(100.dp)

    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = bgColor,
            contentColor = textColor,
            disabledBackgroundColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        elevation = ButtonDefaults.elevation(0.dp),
        border = when (style) {
            ButtonStyle.OUTLINED -> BorderStroke(1.dp, Color.Black)
            ButtonStyle.SAMPLE_CARD -> BorderStroke(3.dp, Color.Yellow)
            else -> null
        },
        contentPadding = PaddingValues(horizontal = paddingHorizontal, vertical = paddingVertical),
        modifier = if (shouldFillMaxWidth) modifier.fillMaxWidth() else modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconRes?.let {
                val iconPainter = painterResource(it)
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize).padding(end = iconPadding)
                )
            }
            Text(
                text = btnText,
                style = getTextStyle(style, fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun getTextStyle(style: ButtonStyle, fontSize: TextUnit): TextStyle {
    return when (style) {
        ButtonStyle.PRIMARY -> TextStyle(fontSize = fontSize, fontWeight = FontWeight.Bold)
        ButtonStyle.PRIMARY_COMPACT -> TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )

        ButtonStyle.SECONDARY -> TextStyle(fontSize = fontSize)
        ButtonStyle.OUTLINED -> TextStyle(fontSize = fontSize)
        ButtonStyle.APP_BAR_ACTION -> TextStyle(fontSize = fontSize)
        ButtonStyle.SAMPLE_CARD -> TextStyle(fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}


@Preview()
@Composable
fun PreviewBtn() {
    Column(Modifier.background(Color.White)) {
        AppCustomButton(
            btnText = "Add New Account",
            onClick = {},
            style = ButtonStyle.PRIMARY,
            modifier = Modifier.padding(16.dp)
        )

        AppCustomButton(
            btnText = "Add New Account",
            onClick = {},
            style = ButtonStyle.PRIMARY_COMPACT,
            modifier = Modifier.padding(16.dp)
        )

        AppCustomButton(
            btnText = "Add New Account",
            onClick = {},
            style = ButtonStyle.SECONDARY,
            modifier = Modifier.padding(16.dp)
        )

        AppCustomButton(
            btnText = "Add New Account",
            onClick = {},
            style = ButtonStyle.OUTLINED,
            modifier = Modifier.padding(16.dp)
        )


        AppCustomButton(
            btnText = "Add New Account",
            onClick = {},
            style = ButtonStyle.APP_BAR_ACTION,
            iconRes = Res.drawable.compose_multiplatform,
            modifier = Modifier.padding(16.dp)
        )

        AppCustomButton(
            btnText = "Ok",
            onClick = {},
            style = ButtonStyle.SAMPLE_CARD,
            modifier = Modifier.padding(16.dp)
        )

        AppCustomButton(
            btnText = "Camera",
            onClick = {},
            style = ButtonStyle.OUTLINED,
            modifier = Modifier.padding(16.dp),
            iconRes = Res.drawable.compose_multiplatform,
        )

        AppCustomButton(
            btnText = "Gallery",
            onClick = {},
            style = ButtonStyle.PRIMARY_COMPACT,
            modifier = Modifier.padding(16.dp),
            iconRes = Res.drawable.compose_multiplatform,
        )
    }
}
