package com.kelvinquantic.danamon.ui.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.room.model.UserDaoModel
import com.kelvinquantic.danamon.ui.common.isValidEmail
import com.kelvinquantic.danamon.ui.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Role {
    User, Admin
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val registerState: StateFlow<LoginState> get() = _state

    fun checkData(
        username: String,
        email: String,
        password: String,
        role: String,
        currUsername: String = "",
        isEdit: Boolean = false,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkInput(username, email, password, role)) {

                val res = roomRepository.findUser(
                    email, username
                )

                if (res.isNotEmpty()) {
                    onRequestError("Email or Username has been registered")
                } else {
                    if (isEdit) {
                        roomRepository.updateUser(currUsername, username, email, password, role)
                    } else {
                        roomRepository.insertUser(
                            UserDaoModel(
                                username = username,
                                email = email,
                                password = password,
                                role = role
                            )
                        )
                    }

                    onRequestSuccess()
                }
            }
        }
    }

    private fun checkInput(
        username: String,
        email: String,
        password: String,
        role: String
    ): Boolean {
        if (username.isEmpty()) {
            onRequestError("Please input your username")
            return false
        } else if (email.isEmpty()) {
            onRequestError(emailMsg = "Please input your email")
            return false
        } else if (!email.isValidEmail()) {
            onRequestError(emailMsg = "Please check your email")
            return false
        } else if (password.isEmpty()) {
            onRequestError(passwordMsg = "Please input your password")
            return false
        } else if (role.isEmpty()) {
            onRequestError("Please choose your role")

            return false
        }
        return true
    }

    private fun onRequestSuccess() {
        _state.value = _state.value.copy(
            isSuccess = true,
        )
    }

    private fun onRequestError(
        usernameMsg: String = "",
        emailMsg: String = "",
        passwordMsg: String = ""
    ) {
        _state.update {
            it.copy(
                usernameError = usernameMsg,
                emailError = emailMsg,
                passError = passwordMsg,
                isSuccess = false,
            )
        }
    }
}
