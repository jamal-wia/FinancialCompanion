package com.financialcompanion.android.greetings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.financialcompanion.android.R
import com.financialcompanion.android.core.domain.extension.cast
import com.financialcompanion.android.core.presentation.base.BaseFragment
import com.financialcompanion.android.core.presentation.ds.DotsIndicator
import com.financialcompanion.android.greetings.presentation.GreetingViewState.Data
import com.financialcompanion.android.greetings.presentation.model.GreetingUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GreetingsFragment : BaseFragment() {

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
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GreetingPager(greetings = data.greeting)
            Spacer(modifier = Modifier.height(height = 30.dp))
            StartButton()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GreetingPager(greetings: List<GreetingUiModel>) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val pagerState = rememberPagerState { greetings.size }
            HorizontalPager(state = pagerState) { currentPage ->
                GreetingPage(greeting = greetings[currentPage])
            }
            Spacer(modifier = Modifier.height(height = 19.dp))
            DotsIndicator(pagerState = pagerState)
        }
    }

    @Composable
    fun GreetingPage(greeting: GreetingUiModel) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = greeting.image),
                contentDescription = null
            )
            Text(
                text = stringResource(id = greeting.titleId),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 45.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                fontSize = 25.sp, textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = greeting.descriptionId),
                modifier = Modifier.padding(top = 13.dp),
                textAlign = TextAlign.Center,
                color = Color(R.color.gray),
                fontSize = 17.sp,
            )
        }
    }

    @Composable
    fun StartButton() {
        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(size = 6.dp),
        ) {
            Text(text = stringResource(id = R.string.start_right_now_button))
        }
    }
}