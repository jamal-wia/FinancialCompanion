package com.financialcompanion.android.greetings.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class GreetingUiModel(
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int,
    @DrawableRes val image: Int
)