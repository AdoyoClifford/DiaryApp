package com.adoyo.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adoyo.diaryapp.presentation.auth.AuthenticationScreen
import com.adoyo.diaryapp.presentation.auth.AuthenticationViewModel
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import java.lang.Exception

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
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        AuthenticationScreen(
            oneTapState = oneTapState,
            loadingState = loadingState,
            messageBarState = messageBarState,
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        if (it)
                            messageBarState.addSuccess("You successfully logged in")
                        viewModel.setLoadingState(false)
                    },
                    onError = { message ->
                        messageBarState.addError(Exception(message))
                        viewModel.setLoadingState(false)
                    }
                )

            },
            onDialogDismissed = {
                messageBarState.addError(Exception(it))
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoadingState(true)
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