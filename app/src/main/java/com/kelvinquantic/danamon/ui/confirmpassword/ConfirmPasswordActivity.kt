package com.kelvinquantic.danamon.ui.confirmpassword

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelvinquantic.danamon.room.model.UserDaoModel
import com.kelvinquantic.danamon.ui.common.LoginTextField
import com.kelvinquantic.danamon.ui.common.findActivity
import com.kelvinquantic.danamon.ui.common.finish
import com.kelvinquantic.danamon.ui.confirmpassword.viewmodel.ConfirmPasswordViewModel
import com.kelvinquantic.danamon.ui.theme.LearningTheme
import com.kelvinquantic.danamon.utils.bigTextStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    ConfirmPasswordScreen()
                }
            }
        }
    }
}

@Composable
fun ConfirmPasswordScreen(vm: ConfirmPasswordViewModel = hiltViewModel()) {
    var passwordText by remember { mutableStateOf(TextFieldValue("")) }
    val ctx = LocalContext.current

    val intent = ctx.findActivity()?.intent

    var sessionData: UserDaoModel? = null

    intent?.let {
        sessionData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelableExtra("data", UserDaoModel::class.java)
        } else {
            it.getParcelableExtra("data")

        }
    }

    val state = vm.confirmPasswordState.collectAsState()

    if (state.value.isSuccess) {
        Toast.makeText(
            ctx,
            "Delete User success",
            Toast.LENGTH_LONG
        ).show()

        ctx.finish()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginTextField(passwordText, "Input Password", isPassword = true,
                onTextChange = { tfv ->
                    passwordText = tfv

                },
                errorMsg = state.value.passError)
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                vm.checkUsername(
                    sessionData?.username ?: "",
                    sessionData?.password ?: "",
                    passwordText.text
                )
            }) {
                Text(text = "Confirm Password", style = bigTextStyle, textAlign = TextAlign.Center)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ConfirmPasswordPreview() {
    LearningTheme {
        Surface {
            ConfirmPasswordScreen()
        }
    }
}
