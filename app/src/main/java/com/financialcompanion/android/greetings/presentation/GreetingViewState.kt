package com.financialcompanion.android.greetings.presentation

import com.financialcompanion.android.R
import com.financialcompanion.android.greetings.presentation.model.GreetingUiModel

sealed class GreetingViewState {
    object Data : GreetingViewState() {
        val greeting = listOf(
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            )
        )
    }
}