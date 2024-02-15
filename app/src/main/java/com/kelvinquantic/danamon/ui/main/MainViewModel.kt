package com.kelvinquantic.danamon.ui.main

import androidx.lifecycle.ViewModel
import com.kelvinquantic.danamon.navigation.AppNavigator
import com.kelvinquantic.danamon.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel

    fun onNavigateToLogin() {
        appNavigator.tryNavigateTo(Destination.LoginScreen())
    }

    fun onNavigateToRegister() {
        appNavigator.tryNavigateTo(Destination.RegisterScreen())
    }

    fun onNavigateToHome() {
        appNavigator.tryNavigateTo(Destination.HomeScreen())
    }
}
