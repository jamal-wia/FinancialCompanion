package com.financialcompanion.android.core.presentation.mvvm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.financialcompanion.android.core.domain.extension.cast
import com.financialcompanion.android.core.presentation.extension.requireAppCompatActivity
import com.jamal_aliev.navigationcontroller.core.NavigationContextChanger
import com.jamal_aliev.navigationcontroller.core.provider.OnNavigationUpProvider
import me.aartikov.alligator.animations.AnimationData
import java.lang.ref.WeakReference

abstract class BaseViewModel : ViewModel() {

    val tag by lazy { "${this::class.simpleName}-@${this.hashCode()}" }

    var activityWeak: WeakReference<AppCompatActivity>? = null

    protected fun requireNavigationContextChanger(): NavigationContextChanger {
        val activity = requireNotNull(activityWeak?.get())
        return activity as? NavigationContextChanger
            ?: activity.supportFragmentManager.fragments
                .first { it is NavigationContextChanger }
                .cast()
    }

    protected fun requireOnNavigationUpProvider(): OnNavigationUpProvider {
        val activity = requireNotNull(activityWeak?.get())
        return activity as? OnNavigationUpProvider
            ?: activity.supportFragmentManager.fragments
                .first { it is OnNavigationUpProvider }
                .cast()
    }

    open fun onAttach(context: Context, baseMvvmFragment: BaseMvvmFragment) {
        activityWeak = WeakReference(baseMvvmFragment.requireAppCompatActivity())
    }

    open fun onCreate(savedInstanceState: Bundle?) {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onSaveInstanceState(outState: Bundle) {}
    open fun onDestroy() {}

    open fun onDetach(baseMvvmFragment: BaseMvvmFragment) {
        activityWeak?.clear()
        activityWeak = null
    }

    /**
     * Делает навигацию назад учитывая вложенность навигации
     * */
    fun onNavigationUp(animationData: AnimationData? = null) {
//        requireOnNavigationUpProvider().onNavigationUp(animationData)
    }
}
