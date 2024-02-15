package com.kelvinquantic.danamon.ui.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelvinquantic.danamon.navigation.Destination
import com.kelvinquantic.danamon.navigation.NavigationHost
import com.kelvinquantic.danamon.navigation.NavigationIntent
import com.kelvinquantic.danamon.navigation.composable
import com.kelvinquantic.danamon.ui.home.HomeActivity
import com.kelvinquantic.danamon.ui.login.LoginActivity
import com.kelvinquantic.danamon.ui.register.RegisterActivity
import com.kelvinquantic.danamon.ui.splash.SplashScreenActivity
import com.kelvinquantic.danamon.ui.theme.DanamonAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavigationEffects(
        navigationChannel = vm.navigationChannel,
        navHostController = navController
    )
    DanamonAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            NavigationHost(
                navController = navController,
                startDestination = Destination.SplashScreen
            ) {
                composable(destination = Destination.LoginScreen) {
                    LoginActivity()
                }
                composable(destination = Destination.RegisterScreen) {
                    RegisterActivity()
                }
                composable(destination = Destination.HomeScreen) {
                    HomeActivity()
                }
                composable(destination = Destination.SplashScreen) {
                    SplashScreenActivity()
                }
            }
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
