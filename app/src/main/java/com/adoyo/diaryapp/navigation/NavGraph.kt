package com.adoyo.diaryapp.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adoyo.diaryapp.data.repository.MongoDB
import com.adoyo.diaryapp.model.Diary
import com.adoyo.diaryapp.presentation.components.DisplayAlertDialog
import com.adoyo.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.adoyo.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.adoyo.diaryapp.presentation.screens.home.HomeScreen
import com.adoyo.diaryapp.presentation.screens.home.HomeViewModel
import com.adoyo.diaryapp.utils.Constants.APP_ID
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.adoyo.diaryapp.utils.RequestState
import com.adoyo.diaryapp.presentation.screens.write.WriteScreen
import com.adoyo.diaryapp.presentation.screens.write.WriteScreenViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetUpNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        authenticationRoute(navigateHome = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        }, onDataLoaded = onDataLoaded)
        homeRoute(navigateToWrite = {
            navHostController.navigate(Screen.Write.route)
        }, navigateToAuth = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Authentication.route)
        },
            onDataLoaded = onDataLoaded,
            navigateToWriteWithArgs = {
                navHostController.navigate(Screen.Write.passDiaryId(it))
                Log.d("theId", it)
            }
        )
        writeRoute(onBackPressed = { navHostController.popBackStack() })
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val isAuthenticated by viewModel.isAuthenticated

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
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


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(Screen.Home.route) {
        var signOutDialogOpened by remember {
            mutableStateOf(false)
        }
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()


        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            onSignOutClicked = {
                signOutDialogOpened = true
            }, navigateToWriteWithArgs = navigateToWriteWithArgs
        )


        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }

        LaunchedEffect(key1 = diaries) {
            if (diaries != RequestState.Loading) {
                onDataLoaded()
            }
        }

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                val user = App.create(APP_ID).currentUser
                if (user != null) {
                    scope.launch(Dispatchers.IO) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            })
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel: WriteScreenViewModel = viewModel()
        val selectedId = viewModel.uiState.selectedDiary
        val uiState = viewModel.uiState

        val pagerState = rememberPagerState()
        WriteScreen(
            uiState = uiState,
            onBackPressed = onBackPressed,
            selectedDiary = null,
            onDeleteConfirmed = {},
            pagerState = pagerState,
            onTitleChanged = {viewModel.setTitle(title = it)},
            onDescriptionChanged = {viewModel.setDescription(description = it)}
        )
    }
}