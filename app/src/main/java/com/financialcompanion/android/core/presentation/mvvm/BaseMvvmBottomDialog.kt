package com.financialcompanion.android.core.presentation.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.financialcompanion.android.core.presentation.base.BaseBottomDialog

abstract class BaseMvvmBottomDialog(
    @LayoutRes layoutRes: Int
) : BaseBottomDialog(layoutRes) {

    abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    open fun setupViewModel() {}

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
