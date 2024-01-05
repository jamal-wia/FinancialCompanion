package com.financialcompanion.android.greetings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.financialcompanion.android.core.domain.extension.cast
import com.financialcompanion.android.core.presentation.base.BaseFragment
import com.financialcompanion.android.greetings.presentation.GreetingViewState.Data
import com.financialcompanion.android.greetings.presentation.model.GreetingUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GreetingsFragment : BaseFragment() {
    // Выяснить имплементацию collectAsStateWithLifecycle()

    private val viewModel by viewModel<GreetingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(ctx).apply {
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            DataState(data = state.cast())
        }
    }

    @Preview
    @Composable
    fun PreviewDataState() {
        DataState(data = Data)
    }

    @Composable
    fun DataState(data: Data) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GreetingPager(greetings = data.greeting)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GreetingPager(greetings: List<GreetingUiModel>) {
        val pagerState = rememberPagerState { greetings.size }
        HorizontalPager(state = pagerState) { currentPage ->
            GreetingPage(greeting = greetings[currentPage])
        }
    }

    @Composable
    fun GreetingPage(greeting: GreetingUiModel) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = greeting.image), contentDescription = null)
            Text(text = stringResource(id = greeting.titleId))
            Text(text = stringResource(id = greeting.descriptionId))
        }
    }
}

