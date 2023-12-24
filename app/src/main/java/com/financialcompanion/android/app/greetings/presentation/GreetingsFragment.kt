package com.financialcompanion.android.app.greetings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financialcompanion.android.R
import com.financialcompanion.android.core.presentation.base.BaseFragment

class GreetingsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(ctx).apply {
        setContent {
            Greetings()
        }
    }
}

@Preview
@Composable
fun Greetings() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable.first_screen),
            contentDescription = "",
        )

        Text(
            text = stringResource(R.string.title_screen_first),
            fontSize = 22.sp, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp)
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.description_screen_first),
            fontSize = 17.sp, modifier = Modifier
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        OutlinedButton(
            onClick = { /*TODO*/ },
            border = BorderStroke(
                1.dp,
                color = colorResource(id = R.color.moody_blue)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 90.dp),
            shape = RoundedCornerShape(10.dp),
        )
        {
            Text(
                text = stringResource(R.string.start_right_now_button)
                    .uppercase(),
                color = colorResource(id = R.color.moody_blue)
            )
        }
    }
}
