package com.kelvinquantic.danamon.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.kelvinquantic.danamon.utils.mediumTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    tfv: TextFieldValue,
    label: String,
    isPassword: Boolean = false,
    onTextChange: (TextFieldValue) -> Unit,
    errorMsg: String = ""
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = tfv,
        label = { Text(text = label) },
        onValueChange = {
            onTextChange(it)
        },
        textStyle = mediumTextStyle,
        visualTransformation = if (isPassword) {
            if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isPassword) ImeAction.Done else ImeAction.Next,
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        isError = errorMsg.isNotEmpty(),
        supportingText = {
            if (errorMsg.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMsg,
                    color = Color.Red
                )
            }
        },
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        }
    )
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return this.matches(emailRegex)
}
