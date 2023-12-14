package com.financialcompanion.android.core.presentation.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment {

    constructor(@LayoutRes layoutRes: Int) : super(layoutRes)
    constructor() : super()

    protected val ctx by lazy { requireContext() }
    protected val res: Resources by lazy { ctx.resources }

    protected var isFirstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstStart = savedInstanceState == null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    open fun setupView(view: View) {}
}