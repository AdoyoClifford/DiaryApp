package com.adoyo.diaryapp.utils

sealed class RequestState<out T> {
    object Idle: RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Error(val error: Throwable): RequestState<Throwable>()
}
