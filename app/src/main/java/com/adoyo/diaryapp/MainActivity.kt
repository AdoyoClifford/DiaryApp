package com.adoyo.diaryapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.adoyo.diaryapp.navigation.Screen
import com.adoyo.diaryapp.navigation.SetUpNavGraph
import com.adoyo.diaryapp.ui.theme.DiaryAppTheme
import com.adoyo.diaryapp.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {


   private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DiaryAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetUpNavGraph(
                        startDestination = getStartRoute(),
                        navHostController = navController,
                        onDataLoaded = {
                            keepSplashOpened = false
                        }
                    )

                }
            }
        }
    }
}

private fun getStartRoute(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
}
