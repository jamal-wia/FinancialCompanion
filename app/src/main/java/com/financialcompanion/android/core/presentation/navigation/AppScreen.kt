package com.financialcompanion.android.core.presentation.navigation

import com.financialcompanion.android.R
import com.financialcompanion.android.greetings.data.prefs.GreetingsPrefs
import com.jamal_aliev.navigationcontroller.controllers.BottomNavigationControllerFragmentScreen
import com.jamal_aliev.navigationcontroller.controllers.LineNavigationControllerFragmentScreen
import com.jamal_aliev.navigationcontroller.core.screen.SwitchScreen
import me.aartikov.alligator.Screen
import java.io.Serializable

sealed class AppScreen : Screen, Serializable {

    class RootNavigationControllerScreen : LineNavigationControllerFragmentScreen(
        screens = run {
            if (GreetingsPrefs.isShowed) {
                listOf(TabNavigationControllerScreen)
            } else {
                listOf(GreetingScreen)
            }
        }
    )

    object TabNavigationControllerScreen : BottomNavigationControllerFragmentScreen(
        menuId = R.menu.navigation_menu,
        screen1 = HomeLineNavigationControllerScreen,
        screen2 = Screen2LineNavigationControllerScreen,
        screen3 = Screen3LineNavigationControllerScreen,
        screen4 = Screen4LineNavigationControllerScreen,
        screen5 = ProfileLineNavigationControllerScreen,
        rootScreen = HomeLineNavigationControllerScreen
    )

    object HomeLineNavigationControllerScreen :
        LineNavigationControllerFragmentScreen(
            listOf(HomeScreen)
        ),
        SwitchScreen {
        override val id: Int get() = R.id.home_item
    }

    object Screen2LineNavigationControllerScreen :
        LineNavigationControllerFragmentScreen(),
        SwitchScreen {
        override val id: Int get() = R.id.screen2_item
    }

    object Screen3LineNavigationControllerScreen :
        LineNavigationControllerFragmentScreen(),
        SwitchScreen {
        override val id: Int get() = R.id.screen3_item
    }

    object Screen4LineNavigationControllerScreen :
        LineNavigationControllerFragmentScreen(),
        SwitchScreen {
        override val id: Int get() = R.id.screen4_item
    }

    object ProfileLineNavigationControllerScreen :
        LineNavigationControllerFragmentScreen(
            listOf(ProfileScreen)
        ),
        SwitchScreen {
        override val id: Int get() = R.id.profile_item
    }

    object HomeScreen : Screen, Serializable
    object ProfileScreen : Screen, Serializable
    object GreetingScreen : Screen, Serializable
}