package com.adoyo.diaryapp.presentation.screens.write

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adoyo.diaryapp.data.repository.MongoDB
import com.adoyo.diaryapp.model.Diary
import com.adoyo.diaryapp.model.Mood
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.adoyo.diaryapp.utils.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId


@RequiresApi(Build.VERSION_CODES.O)
class WriteScreenViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryArgument()
        fetchSelectedDiary()
    }


    private fun getDiaryArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(key = WRITE_SCREEN_ARGUMENT_KEY)
        )
    }


    private fun fetchSelectedDiary() {
        if (uiState.selectedDiaryId != null) {
            viewModelScope.launch {
                MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(uiState.selectedDiaryId!!))
                    .collect {diary ->
                        if (diary is RequestState.Success) {
                            selectedDiary(diary = diary.data)
                            setTitle(title = diary.data.title)
                            setDescription(description = diary.data.description)
                            setMood(mood = Mood.valueOf(diary.data.mood))
                        }
                    }
            }
        }
    }

    fun selectedDiary(diary: Diary) {
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

    fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = MongoDB.insertDiary(diary = diary)
            if (result is RequestState.Success) {
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } else if (result is RequestState.Error) {
                withContext(Dispatchers.Main) {
                    onError(result.error.message.toString())
                }
            }
        }
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy,
)
