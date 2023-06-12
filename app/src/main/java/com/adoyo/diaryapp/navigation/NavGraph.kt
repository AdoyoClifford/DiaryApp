package com.adoyo.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetUpNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination) {

    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(Screen.Authentication.route) {

    }
}

fun NavGraphBuilder.homeRoute() {
    composable(Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(Screen.Write.route) {

    }
}