package com.adoyo.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.adoyo.diaryapp.model.Diary
import com.adoyo.diaryapp.model.Mood
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    moodName: () -> String,
    uiState: UiState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    pagerState: PagerState,
    onSaveClicked: (Diary) -> Unit
) {

    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                moodName = moodName,
                onBackPressed = onBackPressed,
                selectedDiary = uiState.selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed
            )
        },
        content = {
            WriteContent(
                paddingValues = it,
                pagerState = pagerState,
                title = uiState.title,
                onTitleCHanged = onTitleChanged,
                description = uiState.description,
                onDescriptionChange = onDescriptionChanged,
                uiState = uiState,
                onSaveClicked = onSaveClicked
            )
        }
    )
}