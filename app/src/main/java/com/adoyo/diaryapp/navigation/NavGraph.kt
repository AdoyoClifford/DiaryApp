package com.adoyo.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adoyo.diaryapp.presentation.auth.AuthenticationScreen
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetUpNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        AuthenticationScreen(
            oneTapState = oneTapState,
            loadingState = oneTapState.opened,
            onButtonClicked = {
                oneTapState.open()
            })
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}