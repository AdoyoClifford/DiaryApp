package com.adoyo.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.adoyo.diaryapp.model.Diary
import com.adoyo.diaryapp.model.GalleryState
import com.adoyo.diaryapp.model.Mood
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    galleryState: GalleryState,
    moodName: () -> String,
    uiState: UiState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    pagerState: PagerState,
    onSaveClicked: (Diary) -> Unit,
    onUpdatedDateTime: (ZonedDateTime) -> Unit,
    onImageSelect: (Uri) -> Unit
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
                onDeleteConfirmed = onDeleteConfirmed,
                onUpdatedDateTime = onUpdatedDateTime
            )
        },
        content = {
            WriteContent(
                galleryState = galleryState,
                paddingValues = it,
                pagerState = pagerState,
                title = uiState.title,
                onTitleCHanged = {name ->
                    onTitleChanged(name)
                                 Log.d("Name",name)},
                description = uiState.description,
                onDescriptionChange = onDescriptionChanged,
                uiState = uiState,
                onSaveClicked = onSaveClicked,
                onImageSelect = onImageSelect
            )
        }
    )
}