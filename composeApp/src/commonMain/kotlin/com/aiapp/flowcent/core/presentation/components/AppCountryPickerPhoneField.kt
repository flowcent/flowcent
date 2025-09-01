package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.ui.CountryPickerBasicTextField

@Composable
fun AppCountryPickerPhoneField(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onCountrySelected: (CountryDetails) -> Unit,
    modifier: Modifier = Modifier,
    defaultCountryCode: String = "us",
    paddingValues: PaddingValues = PaddingValues(6.dp),
    label: @Composable (() -> Unit)? = null,
    showCountryFlag: Boolean = true,
    showCountryPhoneCode: Boolean = true,
    showCountryName: Boolean = false,
    showCountryCode: Boolean = false,
    showArrowDropDown: Boolean = true,
    spaceAfterCountryFlag: Dp = 8.dp,
    spaceAfterCountryPhoneCode: Dp = 6.dp,
    spaceAfterCountryName: Dp = 6.dp,
    spaceAfterCountryCode: Dp = 6.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    focusedBorderThickness: Dp = 2.dp,
    unfocusedBorderThickness: Dp = 1.dp,
    verticalDividerColor: Color = Color(0xFFDDDDDD),
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        cursorColor = Color.Black,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
) {
    CountryPickerBasicTextField(
        mobileNumber = mobileNumber,
        defaultCountryCode = defaultCountryCode,
        onMobileNumberChange = onMobileNumberChange,
        onCountrySelected = onCountrySelected,
        modifier = modifier,
        defaultPaddingValues = paddingValues,
        label = label,
        showCountryFlag = showCountryFlag,
        showCountryPhoneCode = showCountryPhoneCode,
        showCountryName = showCountryName,
        showCountryCode = showCountryCode,
        showArrowDropDown = showArrowDropDown,
        spaceAfterCountryFlag = spaceAfterCountryFlag,
        spaceAfterCountryPhoneCode = spaceAfterCountryPhoneCode,
        spaceAfterCountryName = spaceAfterCountryName,
        spaceAfterCountryCode = spaceAfterCountryCode,
        shape = shape,
        focusedBorderThickness = focusedBorderThickness,
        unfocusedBorderThickness = unfocusedBorderThickness,
        verticalDividerColor = verticalDividerColor,
        colors = textFieldColors
    )
}
