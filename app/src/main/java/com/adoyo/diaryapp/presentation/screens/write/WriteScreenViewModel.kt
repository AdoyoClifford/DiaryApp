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
import com.adoyo.diaryapp.model.Mood
import com.adoyo.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.adoyo.diaryapp.utils.RequestState
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId

@RequiresApi(Build.VERSION_CODES.O)
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


    private fun fetchSelectedDiary() {
        viewModelScope.launch {
            if (uiState.selectedDiary != null) {
                val diary = MongoDB.getSelectedDiary(
                    diaryId = BsonObjectId(uiState.selectedDiary!!))

                if (diary is RequestState.Success) {
                    setTitle(title = diary.data.title)
                    setDescription(description = diary.data.description)
                    setMood(mood = Mood.valueOf(diary.data.mood))
                }
            }
        }
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
}

data class UiState(
    val selectedDiary: String? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy,
)