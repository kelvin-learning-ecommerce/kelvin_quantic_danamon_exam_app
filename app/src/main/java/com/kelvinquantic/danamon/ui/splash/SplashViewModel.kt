package com.kelvinquantic.danamon.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.ui.common.freshStartActivity
import com.kelvinquantic.danamon.ui.login.LoginActivity
import com.kelvinquantic.danamon.ui.photolist.HomeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {


    fun isUserLogin(ctx: Context) {
        viewModelScope.launch {

            delay(2000)

            val loginData = roomRepository.getLoginData()
            ctx.freshStartActivity(if (loginData.isNotEmpty()) HomeActivity::class.java else LoginActivity::class.java)
        }
    }
}
