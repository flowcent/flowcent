package com.aiapp.flowcent.core.component.countryCodePicker.model

import org.jetbrains.compose.resources.DrawableResource

data class CountryDetails(
    var countryCode: String,
    val countryPhoneNumberCode: String,
    val countryName: String,
    val countryFlag: DrawableResource
)