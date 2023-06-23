package com.adoyo.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.adoyo.diaryapp.model.Diary
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    onBackPressed: () -> Unit,
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit,
    pagerState: PagerState
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                onBackPressed = onBackPressed,
                selectedDiary = selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed
            )
        },
        content = {
            WriteContent(
                paddingValues = it,
                pagerState = pagerState,
                title = "",
                onTitleCHanged = {},
                description = "",
                onDescriptionChange = {}
            )
        }
    )
}