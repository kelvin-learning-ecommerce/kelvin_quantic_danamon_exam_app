package com.kelvinquantic.danamon.ui.login

import android.os.Bundle
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
import com.kelvinquantic.danamon.ui.common.LoginTextField
import com.kelvinquantic.danamon.ui.common.freshStartActivity
import com.kelvinquantic.danamon.ui.home.HomeActivity
import com.kelvinquantic.danamon.ui.login.viewmodel.LoginViewModel
import com.kelvinquantic.danamon.ui.theme.DanamonAppTheme
import com.kelvinquantic.danamon.utils.bigTextStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DanamonAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    LoginPageScreen()
                }
            }
        }
    }
}

@Composable
fun LoginPageScreen(vm: LoginViewModel = hiltViewModel()) {
    val state = vm.loginState.collectAsState()

    var ctx = LocalContext.current

    var usernameText by remember { mutableStateOf(TextFieldValue("")) }
    var passwordText by remember { mutableStateOf(TextFieldValue("")) }

    if (state.value.isSuccess) {
        ctx.freshStartActivity(HomeActivity::class.java)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginTextField(
                usernameText, "Input Username or Email",
                onTextChange = { tfv ->
                    usernameText = tfv

                },
                errorMsg = state.value.usernameError
            )
            Spacer(modifier = Modifier.height(10.dp))
            LoginTextField(passwordText, "Input Password", isPassword = true,
                onTextChange = { tfv ->
                    passwordText = tfv

                })
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                vm.checkUsername(usernameText.text, passwordText.text)
            }) {
                Text(text = "Login", style = bigTextStyle, textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                vm.openRegister()
            }) {
                Text(text = "Register", style = bigTextStyle, textAlign = TextAlign.Center)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    DanamonAppTheme {
        Surface {
            LoginPageScreen()
        }
    }
}
