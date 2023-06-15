@file:OptIn(ExperimentalMaterial3Api::class)

package com.adoyo.diaryapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adoyo.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.adoyo.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.adoyo.diaryapp.presentation.screens.home.HomeScreen
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun SetUpNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        authenticationRoute(navigateHome = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        })
        homeRoute(navigateToWrite = {
            navHostController.navigate(Screen.Write.route)
        })
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateHome: () -> Unit
) {
    composable(Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val isAuthenticated by viewModel.isAuthenticated
        AuthenticationScreen(
            oneTapState = oneTapState,
            loadingState = loadingState,
            messageBarState = messageBarState,
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
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
                viewModel.setLoadingState(false)
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoadingState(true)
            },
            isAuthenticated = isAuthenticated,
            navigateHome = navigateHome
        )
    }
}



fun NavGraphBuilder.homeRoute(navigateToWrite: () -> Unit) {
    composable(Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        HomeScreen(
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            onSignOutClicked = {})
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