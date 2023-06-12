package com.adoyo.diaryapp.presentation.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.adoyo.diaryapp.utils.Constants.CLIENT_ID
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    onButtonClicked: () -> Unit
) {
    Scaffold(
        content = {
            AuthenticationContent(loadingState = loadingState, onButtonClicked = onButtonClicked)
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { token ->
            Log.d("Auth", token)
        },
        onDialogDismissed = { message ->
            Log.d("Auth", message)

        }
    )
}