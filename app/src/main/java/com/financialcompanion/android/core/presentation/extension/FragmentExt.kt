package com.financialcompanion.android.core.presentation.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.requireAppCompatActivity() =
    requireActivity() as AppCompatActivity

