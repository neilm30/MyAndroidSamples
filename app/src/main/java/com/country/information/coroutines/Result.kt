package com.country.information.coroutines

sealed class Result<out T : Any>

class OnSuccess<out T : Any>(private val data: T) : Result<T>() {
    fun get(): T = data
}

class OnError(val exception: Throwable, val message: String? = exception.localizedMessage) : Result<Nothing>()

class OnCancelException(val exception: Throwable) : Result<Nothing>()