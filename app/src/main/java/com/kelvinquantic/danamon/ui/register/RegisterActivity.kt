package com.kelvinquantic.danamon.ui.register

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.kelvinquantic.danamon.room.daomodel.UserDaoModel
import com.kelvinquantic.danamon.ui.common.LargeDropdownMenu
import com.kelvinquantic.danamon.ui.common.LoginTextField
import com.kelvinquantic.danamon.ui.common.findActivity
import com.kelvinquantic.danamon.ui.common.finish
import com.kelvinquantic.danamon.ui.register.viewmodel.RegisterViewModel
import com.kelvinquantic.danamon.ui.register.viewmodel.Role
import com.kelvinquantic.danamon.ui.theme.DanamonAppTheme
import com.kelvinquantic.danamon.utils.bigTextStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DanamonAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    RegisterPageScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPageScreen(vm: RegisterViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val state = vm.registerState.collectAsState()

    val intent = context.findActivity()?.intent

    var isEdit = false
    var sessionData: UserDaoModel? = null

    intent?.let {
        isEdit = it.getBooleanExtra("isEdit", false)
        sessionData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelableExtra("data", UserDaoModel::class.java)
        } else {
            it.getParcelableExtra("data")

        }
    }

    var usernameText by remember { mutableStateOf(TextFieldValue(sessionData?.username ?: "")) }
    var emailText by remember { mutableStateOf(TextFieldValue(sessionData?.email ?: "")) }
    var passwordText by remember { mutableStateOf(TextFieldValue("")) }

    var userRole = -1

    sessionData?.let {
        userRole = if (it.role == Role.Admin.toString()) {
            1
        } else {
            0
        }
    }

    val roles = listOf(Role.User.toString(), Role.Admin.toString())
    var selectedIndex by remember { mutableIntStateOf(userRole) }

    if (state.value.isSuccess) {
        Toast.makeText(
            context, if (!isEdit)
                "Register success, please login with registered User" else "Edit sser data success",
            Toast.LENGTH_LONG
        ).show()

        context.finish()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = if (!isEdit) "Register" else "Edit User", style = bigTextStyle)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        context.finish()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginTextField(
                        usernameText, "Input Username",
                        onTextChange = { tfv ->
                            usernameText = tfv

                        },
                        errorMsg = state.value.usernameError
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LoginTextField(
                        emailText, "Input Email",
                        onTextChange = { tfv ->
                            emailText = tfv

                        },
                        errorMsg = state.value.emailError
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    LoginTextField(
                        passwordText, "Input Password", isPassword = true,
                        onTextChange = { tfv ->
                            passwordText = tfv

                        },
                        errorMsg = state.value.passError
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    LargeDropdownMenu(
                        label = "Role",
                        items = roles,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index, _ -> selectedIndex = index },
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(onClick = {
                        vm.checkData(
                            usernameText.text,
                            emailText.text,
                            passwordText.text,
                            if (selectedIndex == -1) "" else roles[selectedIndex],
                            currUsername = sessionData?.username ?: "",
                            isEdit
                        )
                    }) {
                        Text(text = if (!isEdit) "Register" else "Edit", style = bigTextStyle, textAlign = TextAlign.Center)
                    }

                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    DanamonAppTheme {
        Surface {
            RegisterPageScreen()
        }
    }
}
