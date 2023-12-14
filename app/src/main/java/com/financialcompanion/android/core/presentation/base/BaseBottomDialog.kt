package com.financialcompanion.android.core.presentation.base

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomDialog(
    @LayoutRes private val layoutRes: Int
) : BottomSheetDialogFragment() {

    protected val ctx by lazy { requireContext() }
    protected val res: Resources by lazy { ctx.resources }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    open fun setupView(view: View) {}
}