package com.financialcompanion.android.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financialcompanion.android.R
import com.financialcompanion.android.core.presentation.base.BaseFragment
import com.financialcompanion.android.core.presentation.theme.DodgerBlue
import com.financialcompanion.android.core.presentation.theme.Dove_Gray
import com.financialcompanion.android.core.presentation.theme.Gallery
import com.financialcompanion.android.core.presentation.theme.Gray
import com.financialcompanion.android.core.presentation.theme.Monte_Carlo
import com.financialcompanion.android.core.presentation.theme.Mountain_Meadow
import com.financialcompanion.android.core.presentation.theme.Porcelain
import com.financialcompanion.android.core.presentation.theme.Silver
import com.financialcompanion.android.core.presentation.theme.Silver_Chalice

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
                .background(color = Gallery), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_go_back),
                    contentDescription = null,
                    modifier = Modifier.padding(13.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.email_img),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(22.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 49.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.create_new_account),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.email),
                    color = Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                ClickableText(text = buildAnnotatedString {
                    Text(
                        text = stringResource(id = R.string.click_text)
                            .uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Mountain_Meadow,

                        )

                }, onClick = { Toast.makeText(context, "Text", Toast.LENGTH_SHORT).show() })

                Text(
                    text = stringResource(id = R.string.password),
                    color = Gray,
                    fontSize = 12.sp
                )

                val text = remember { mutableStateOf(MINIMUM_SYMBOL) }

                OutlinedTextField(
                    text.value,
                    onValueChange = { text.value },
                    textStyle = TextStyle(color = Silver_Chalice, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 0.dp, color = Silver),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_show),
                            contentDescription = null,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Monte_Carlo)
                ) {
                    Text(
                        text = stringResource(id = R.string.create_account)
                            .uppercase(),
                        color = Porcelain, fontSize = 19.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {

                    Text(
                        text = stringResource(id = R.string.have_account),
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = Dove_Gray, fontSize = 17.sp
                    )

                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.singn_in)),
                        onClick = {},
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        style = TextStyle(color = DodgerBlue, fontSize = 15.sp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }

    companion object {
        const val MINIMUM_SYMBOL = "Минимум 6 символов"
    }
}