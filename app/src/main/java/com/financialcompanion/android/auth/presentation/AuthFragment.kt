package com.financialcompanion.android.auth.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financialcompanion.android.R
import com.financialcompanion.android.core.presentation.base.BaseFragment
import com.financialcompanion.android.core.presentation.theme.AzureRadiance
import com.financialcompanion.android.core.presentation.theme.Black
import com.financialcompanion.android.core.presentation.theme.DodgerBlue
import com.financialcompanion.android.core.presentation.theme.Gallery
import com.financialcompanion.android.core.presentation.theme.Gray
import com.financialcompanion.android.core.presentation.theme.PictonBlue

class AuthFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(ctx).apply {
        setContent {
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DataState() {
        Auth()
    }

    @Composable
    fun Auth() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.shamrock))
                    .fillMaxWidth()
                    .height(190.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_auth),
                    contentDescription = null,
                )
            }

            Text(
                text = stringResource(id = R.string.description_text_auth),
                modifier = Modifier
                    .padding(top = 18.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 2.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzureRadiance),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(id = R.drawable.ic_google),
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        bottomStart = 10.dp
                                    )
                                ),
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(id = R.string.sign_in_google_text_auth)
                                .uppercase(),
                            fontSize = 15.sp,
                        )
                        Spacer(modifier = Modifier)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 2.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzureRadiance),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(id = R.drawable.ic_facebook),
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        bottomStart = 10.dp
                                    )
                                )
                                .height(48.dp),
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(id = R.string.sign_in_facebook_text_auth)
                                .uppercase(),
                            fontSize = 15.sp,
                        )
                        Spacer(modifier = Modifier)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 2.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzureRadiance),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(id = R.drawable.ic_email),
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        bottomStart = 10.dp
                                    )
                                )
                                .padding(3.dp),
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(id = R.string.registration_text_auth)
                                .uppercase(),
                            fontSize = 15.sp,
                        )
                        Spacer(modifier = Modifier)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val context = LocalContext.current
            val part = stringResource(id = R.string.login_to_account).uppercase()
            val textAnnotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(text = stringResource(id = R.string.title_text_auth))
                }

                append(" ")

                withStyle(
                    style = SpanStyle(
                        color = DodgerBlue,
                        fontSize = 15.sp,
                    )
                ) {
                    append(
                        text = stringResource(id = R.string.login_to_account).uppercase()
                    )
                }
            }

            ClickableText(
                text = textAnnotatedString,
            ) { offset ->
                val str = textAnnotatedString.text
                val start = str.indexOf(part)
                val end = start + part.length
                val range = start..end
                if (range.contains(offset)) {
                    Toast.makeText(context, "Text True", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Gallery)
                    .padding(horizontal = 10.dp),

                ) {
                val rulesOfUsePart = stringResource(
                    id = R.string.rules_of_use
                )
                val privacyPolicyPart = stringResource(
                    id = R.string.privacy_policy
                )

                val privacyBuildAnnotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Gray,
                            fontSize = 15.sp,

                            )
                    ) {
                        append(text = stringResource(id = R.string.privacy_policy_description))
                    }
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = PictonBlue,
                            fontSize = 15.sp
                        )
                    ) {
                        append(text = stringResource(id = R.string.rules_of_use))
                    }
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = Gray,
                            fontSize = 15.sp
                        )
                    ) {
                        append(text = stringResource(id = R.string.and))
                    }

                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = PictonBlue,
                            fontSize = 15.sp
                        )
                    ) {
                        append(text = stringResource(id = R.string.privacy_policy))
                    }

                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = Gray,
                            fontSize = 15.sp
                        )
                    ) {
                        append(text = stringResource(id = R.string.privacy_policy_description_part2))
                    }
                }
                ClickableText(
                    text = privacyBuildAnnotatedString,
                    style = TextStyle(textAlign = TextAlign.Center),
                ) { offset ->
                    val str = privacyBuildAnnotatedString.text
                    val start = str.indexOf(rulesOfUsePart)
                    val end = start + rulesOfUsePart.length
                    val range = start..end
                    val strSecond = privacyBuildAnnotatedString.text
                    val startSecond = strSecond.indexOf(privacyPolicyPart)
                    val endSecond = startSecond + privacyPolicyPart.length
                    val rangeSecond = startSecond..endSecond

                    if (range.contains(offset)) {
                        Toast.makeText(context, "Click Successful", Toast.LENGTH_SHORT).show()
                    }
                    if (rangeSecond.contains(offset)) {
                        Toast.makeText(context, "Policy Privacy", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}