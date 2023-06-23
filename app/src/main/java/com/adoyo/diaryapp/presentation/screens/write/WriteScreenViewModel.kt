package com.adoyo.diaryapp.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.adoyo.diaryapp.model.Mood
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY

class WriteScreenViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryArgument()
    }

    private fun getDiaryArgument() {
        uiState = uiState.copy(
            selectedDiary = savedStateHandle.get<String>(key = WRITE_SCREEN_ARGUMENT_KEY)
        )
    }

}

data class UiState(
    val selectedDiary: String? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy,

    )