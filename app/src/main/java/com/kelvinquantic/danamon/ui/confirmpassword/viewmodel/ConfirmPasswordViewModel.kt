package com.kelvinquantic.danamon.ui.confirmpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.ui.confirmpassword.state.ConfirmPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmPasswordViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ConfirmPasswordState())
    val confirmPasswordState: StateFlow<ConfirmPasswordState> get() = _state

    fun checkUsername(username: String, savedPassword: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if (checkInput(savedPassword, password)) {

                roomRepository.deleteUser(username)

                onRequestSuccess()
            }
        }
    }

    private fun checkInput(savedPassword: String, password: String): Boolean {
        var isError = true
        if (savedPassword != password) {
            onRequestError()
            isError = false

        }
        return isError
    }

    private fun onRequestSuccess() {
        _state.value = _state.value.copy(
            isSuccess = true,
        )
    }

    private fun onRequestError() {
        _state.update {
            it.copy(
                passError = "Please input the correct Password",
                isSuccess = false,
            )
        }
    }
}
