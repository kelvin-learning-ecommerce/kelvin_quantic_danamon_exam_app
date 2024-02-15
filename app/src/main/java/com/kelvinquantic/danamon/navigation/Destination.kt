package com.kelvinquantic.danamon.navigation

const val loginRoute = "LoginRoute"
const val registerRoute = "RegisterRoute"
const val splashScreenRoute = "SplashScreenRoute"
const val homeRoute = "HomeRoute"

sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object LoginScreen : NoArgumentsDestination(loginRoute)

    object RegisterScreen : NoArgumentsDestination(registerRoute)
    object SplashScreen : NoArgumentsDestination(splashScreenRoute)
    object HomeScreen : NoArgumentsDestination(homeRoute)

//    object PhotoListScreen : Destination(photoListRoute, "firstName", "lastName") {
//        const val FIST_NAME_KEY = "firstName"
//        const val LAST_NAME_KEY = "lastName"
//
//        operator fun invoke(fistName: String, lastName: String): String = route.appendParams(
//            FIST_NAME_KEY to fistName,
//            LAST_NAME_KEY to lastName
//        )
//    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}
