package com.adoyo.diaryapp.data.repository

import com.adoyo.diaryapp.model.Diary
import com.adoyo.diaryapp.utils.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
}