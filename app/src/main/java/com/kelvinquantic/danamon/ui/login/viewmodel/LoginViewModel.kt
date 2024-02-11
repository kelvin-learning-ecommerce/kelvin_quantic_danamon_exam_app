package com.kelvinquantic.danamon.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.room.daomodel.SessionDaoModel
import com.kelvinquantic.danamon.room.daomodel.UserDaoModel
import com.kelvinquantic.danamon.ui.common.isValidEmail
import com.kelvinquantic.danamon.ui.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _state

    fun checkUsername(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            //return pair, first val is check is email or username, second val is input credential is correct
            val pair = checkInput(username, password)
            if (pair.second) {

                val res =
                    if (pair.first) roomRepository.findUserByEmail(username) else roomRepository.findUserByUsername(
                        username
                    )

                if (res.isEmpty()) {
                    onRequestError("Please check your login data")
                } else {
                    val data = res.first()

                    if (data.password != password) {
                        //error message is same with no username found to avoid credential fraud
                        onRequestError("Please check your login data")
                    } else {
                        onRequestSuccess(data)
                    }
                }
            }
        }
    }

    fun checkInput(username: String, password: String): Pair<Boolean, Boolean> {
        var isEmail = false
        var isError = true
        if (username.isEmpty() || password.isEmpty()) {
            onRequestError("Please input your Username & Password")
            isError = false

        } else if (username.contains("@")) {
            isEmail = true

            if (!username.isValidEmail()) {
                onRequestError("Please check your email")
                isError = false
            }
        }
        return Pair(isEmail, isError)
    }

    internal fun onRequestSuccess(res: UserDaoModel) {
        roomRepository.insertLoginData(
            SessionDaoModel(
                isLogin = true,
                username = res.username,
                email = res.email,
                role = res.role
            )
        )
        _state.value = _state.value.copy(
            isSuccess = true,
        )
    }

    internal fun onRequestError(
        message: String?
    ) {
        _state.update {
            it.copy(
                usernameError = message ?: "Unexpected Error",
                isSuccess = false,
            )
        }
    }

    fun updateState(
        isSuccess: Boolean = false,
        error: String = ""
    ) {
        _state.update {
            it.copy(
                isSuccess = isSuccess,
                usernameError = error
            )
        }
    }
}
