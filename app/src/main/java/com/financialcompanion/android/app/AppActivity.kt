package com.financialcompanion.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.financialcompanion.android.R
import com.jamal_aliev.navigationcontroller.controllers.LineNavigationControllerFragmentScreen
import com.jamal_aliev.navigationcontroller.core.NavigationControllerFragment
import com.jamal_aliev.navigationcontroller.core.provider.OnNavigationUpProvider

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        if (savedInstanceState == null) {
            NavigationControllerFragment.Builder()
                .setRootScreen(LineNavigationControllerFragmentScreen())
                .show(supportFragmentManager, R.id.app_container)
        }
    }

    override fun onNavigateUp(): Boolean {
        return (supportFragmentManager.findFragmentById(R.id.app_container)
                as? OnNavigationUpProvider)?.onNavigationUp() == true
    }

}