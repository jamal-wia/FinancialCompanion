package com.financialcompanion.android.core.presentation.navigation

import com.financialcompanion.android.core.presentation.navigation.AppScreen.GreetingScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.HomeLineNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.HomeScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.ProfileLineNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.ProfileScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.RootNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.Screen2LineNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.Screen3LineNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.Screen4LineNavigationControllerScreen
import com.financialcompanion.android.core.presentation.navigation.AppScreen.TabNavigationControllerScreen
import com.financialcompanion.android.greetings.presentation.GreetingsFragment
import com.financialcompanion.android.home.presentation.HomeFragment
import com.financialcompanion.android.profile.presentation.ProfileFragment
import com.jamal_aliev.navigationcontroller.controllers.BottomNavigationControllerFragment
import com.jamal_aliev.navigationcontroller.controllers.LineNavigationControllerFragment
import com.jamal_aliev.navigationcontroller.core.NavigationControllerFactory

class AppNavigationFactory : NavigationControllerFactory() {
    init {
        registerFragment(
            RootNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )
        registerFragment(
            TabNavigationControllerScreen::class.java,
            BottomNavigationControllerFragment::class.java
        )
        registerFragment(
            HomeLineNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )
        registerFragment(
            Screen2LineNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )
        registerFragment(
            Screen3LineNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )
        registerFragment(
            Screen4LineNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )
        registerFragment(
            ProfileLineNavigationControllerScreen::class.java,
            LineNavigationControllerFragment::class.java
        )

        registerFragment(
            HomeScreen::class.java,
            HomeFragment::class.java
        )
        registerFragment(
            ProfileScreen::class.java,
            ProfileFragment::class.java
        )
        registerFragment(
            GreetingScreen::class.java,
            GreetingsFragment::class.java
        )
    }
}