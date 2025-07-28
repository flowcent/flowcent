package com.aiapp.flowcent.core.presentation.components.countryCodePicker.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails

@Composable
fun CountryPickerBasicTextField(
    mobileNumber: String,
    defaultCountryCode: String,
    onMobileNumberChange: (String) -> Unit,
    onCountrySelected: (country: CountryDetails) -> Unit,
    modifier: Modifier = Modifier,
    defaultPaddingValues: PaddingValues = PaddingValues(4.dp, 6.dp),
    showCountryFlag: Boolean = true,
    showCountryPhoneCode: Boolean = true,
    showCountryName: Boolean = false,
    showCountryCode: Boolean = false,
    showArrowDropDown: Boolean = true,
    spaceAfterCountryFlag: Dp = 8.dp,
    spaceAfterCountryPhoneCode: Dp = 6.dp,
    spaceAfterCountryName: Dp = 6.dp,
    spaceAfterCountryCode: Dp = 6.dp,
    countryFlagSize: Dp = 26.dp,
    showVerticalDivider: Boolean = true,
    spaceAfterVerticalDivider: Dp = 4.dp,
    verticalDividerColor: Color = MaterialTheme.colorScheme.onSurface,
    verticalDividerHeight: Dp = 26.dp,
    countryPhoneCodeTextStyle: TextStyle = TextStyle(),
    countryNameTextStyle: TextStyle = TextStyle(),
    countryCodeTextStyle: TextStyle = TextStyle(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable() (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    prefix: @Composable() (() -> Unit)? = null,
    suffix: @Composable() (() -> Unit)? = null,
    supportingText: @Composable() (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(12.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    focusedBorderThickness: Dp = 2.dp,
    unfocusedBorderThickness: Dp = 1.dp,
    onDone: () -> Unit = {},
) {
    PickerBasicTextField(
        value = mobileNumber,
        onValueChange = { updatedMobileNumber -> onMobileNumberChange(updatedMobileNumber) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = {
            CountryPicker(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                defaultCountryCode = defaultCountryCode,
                defaultPaddingValues = defaultPaddingValues,
                showCountryFlag = showCountryFlag,
                showCountryPhoneCode = showCountryPhoneCode,
                showCountryName = showCountryName,
                showCountryCode = showCountryCode,
                showArrowDropDown = showArrowDropDown,
                spaceAfterCountryFlag = spaceAfterCountryFlag,
                spaceAfterCountryPhoneCode = spaceAfterCountryPhoneCode,
                spaceAfterCountryName = spaceAfterCountryName,
                spaceAfterCountryCode = spaceAfterCountryCode,
                countryFlagSize = countryFlagSize,
                showVerticalDivider = showVerticalDivider,
                spaceAfterVerticalDivider = spaceAfterVerticalDivider,
                verticalDividerColor = verticalDividerColor,
                verticalDividerHeight = verticalDividerHeight,
                countryPhoneCodeTextStyle = countryPhoneCodeTextStyle,
                countryNameTextStyle = countryNameTextStyle,
                countryCodeTextStyle = countryCodeTextStyle,
            ) { selectedCountry ->
                onCountrySelected(selectedCountry)
            }
        },
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            },
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        focusedBorderThickness = focusedBorderThickness,
        unfocusedBorderThickness = unfocusedBorderThickness,
    )
}

@Composable
fun CountryPickerTextField(
    mobileNumber: String,
    defaultCountryCode: String,
    onMobileNumberChange: (String) -> Unit,
    onCountrySelected: (country: CountryDetails) -> Unit,
    modifier: Modifier = Modifier,
    defaultPaddingValues: PaddingValues = PaddingValues(4.dp, 6.dp),
    showCountryFlag: Boolean = true,
    showCountryPhoneCode: Boolean = true,
    showCountryName: Boolean = false,
    showCountryCode: Boolean = false,
    showArrowDropDown: Boolean = true,
    spaceAfterCountryFlag: Dp = 8.dp,
    spaceAfterCountryPhoneCode: Dp = 6.dp,
    spaceAfterCountryName: Dp = 6.dp,
    spaceAfterCountryCode: Dp = 6.dp,
    countryFlagSize: Dp = 24.dp,
    showVerticalDivider: Boolean = true,
    spaceAfterVerticalDivider: Dp = 4.dp,
    verticalDividerColor: Color = MaterialTheme.colorScheme.onSurface,
    verticalDividerHeight: Dp = 26.dp,
    countryPhoneCodeTextStyle: TextStyle = TextStyle(),
    countryNameTextStyle: TextStyle = TextStyle(),
    countryCodeTextStyle: TextStyle = TextStyle(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    prefix: @Composable() (() -> Unit)? = null,
    suffix: @Composable() (() -> Unit)? = null,
    supportingText: @Composable() (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onDone: () -> Unit = {},
) {
    PickerTextField(
        value = mobileNumber,
        onValueChange = { updatedMobileNumber -> onMobileNumberChange(updatedMobileNumber) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = {
            CountryPicker(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                defaultCountryCode = defaultCountryCode,
                defaultPaddingValues = defaultPaddingValues,
                showCountryFlag = showCountryFlag,
                showCountryPhoneCode = showCountryPhoneCode,
                showCountryName = showCountryName,
                showCountryCode = showCountryCode,
                showArrowDropDown = showArrowDropDown,
                spaceAfterCountryFlag = spaceAfterCountryFlag,
                spaceAfterCountryPhoneCode = spaceAfterCountryPhoneCode,
                spaceAfterCountryName = spaceAfterCountryName,
                spaceAfterCountryCode = spaceAfterCountryCode,
                countryFlagSize = countryFlagSize,
                showVerticalDivider = showVerticalDivider,
                spaceAfterVerticalDivider = spaceAfterVerticalDivider,
                verticalDividerColor = verticalDividerColor,
                verticalDividerHeight = verticalDividerHeight,
                countryPhoneCodeTextStyle = countryPhoneCodeTextStyle,
                countryNameTextStyle = countryNameTextStyle,
                countryCodeTextStyle = countryCodeTextStyle,
            ) { selectedCountry ->
                onCountrySelected(selectedCountry)
            }
        },
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            },
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        colors = colors,
    )
}