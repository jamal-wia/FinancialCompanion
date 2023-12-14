package com.financialcompanion.android.core.presentation.mvvm

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


abstract class BaseMvvmActivity : AppCompatActivity {

    protected abstract val viewModel: BaseViewModel
    private var viewInflater: ((Context) -> View)? = null

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewInflater != null) setContentView(viewInflater?.invoke(this))
    }
}