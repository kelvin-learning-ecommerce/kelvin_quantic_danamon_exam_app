package com.kelvinquantic.danamon.ui.login.state

data class LoginState(
    val isSuccess: Boolean = false,
    val usernameError: String = "",
    val emailError: String = "",
    val passError: String = ""
)
