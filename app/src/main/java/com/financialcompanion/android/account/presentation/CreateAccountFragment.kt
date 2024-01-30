package com.financialcompanion.android.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.financialcompanion.android.core.presentation.base.BaseFragment
import com.financialcompanion.android.core.presentation.theme.White

class CreateAccountFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(ctx).apply {
        setContent {
            CreatingAccount()
        }
    }

    @Preview()
    @Composable
    fun CreatingAccount() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
        ) {
        }
    }
}