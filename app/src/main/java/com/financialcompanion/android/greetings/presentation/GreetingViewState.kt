package com.financialcompanion.android.greetings.presentation

import com.financialcompanion.android.R
import com.financialcompanion.android.greetings.presentation.model.GreetingUiModel

sealed class GreetingViewState {
    object Data : GreetingViewState() {
        val greetings = listOf(
            GreetingUiModel(
                titleId = R.string.title_screen_first,
                descriptionId = R.string.description_screen_first,
                image = R.drawable.first_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_second,
                descriptionId = R.string.description_screen_second,
                image = R.drawable.second_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_third,
                descriptionId = R.string.description_screen_third,
                image = R.drawable.third_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_fourth,
                descriptionId = R.string.description_screen_fourth,
                image = R.drawable.fourth_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_fifth,
                descriptionId = R.string.description_screen_fifth,
                image = R.drawable.fifth_screen
            ),
            GreetingUiModel(
                titleId = R.string.title_screen_sixth,
                descriptionId = R.string.description_screen_sixth,
                image = R.drawable.sixth_screen
            )
        )
    }
}