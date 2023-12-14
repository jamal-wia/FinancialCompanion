package com.financialcompanion.android.core.presentation.mvvm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.financialcompanion.android.core.presentation.extension.requireAppCompatActivity
import me.aartikov.alligator.animations.AnimationData
import java.lang.ref.WeakReference

abstract class BaseViewModel : ViewModel() {

    val tag by lazy { "${this::class.simpleName}-@${this.hashCode()}" }

    var activityWeak: WeakReference<AppCompatActivity>? = null

//    protected fun requireStatusBarChanger() =
//        activityWeak?.get() as StatusBarChanger

//    protected fun requireNavigationContextChanger() =
//        activityWeak?.get() as com.financialcompanion.ultnavigation.global.NavigationContextChanger
//
//    protected fun requireOnNavigationUpProvider() =
//        activityWeak?.get() as com.financialcompanion.ultnavigation.global.OnNavigationUpProvider

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
