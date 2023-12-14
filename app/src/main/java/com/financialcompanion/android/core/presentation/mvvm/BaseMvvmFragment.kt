package com.financialcompanion.android.core.presentation.mvvm

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.financialcompanion.android.core.presentation.base.BaseFragment

abstract class BaseMvvmFragment : BaseFragment {

    constructor(@LayoutRes layoutRes: Int) : super(layoutRes)
    constructor() : super()

    abstract val viewModel: BaseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.onAttach(context, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        viewModel.onDetach(this)
        super.onDetach()
    }

    open fun setupViewModel() {}
}
