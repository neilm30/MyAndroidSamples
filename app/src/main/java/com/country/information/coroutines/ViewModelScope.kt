package com.country.information.coroutines

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

interface ViewModelScope : CoroutineScope {
    companion object {
        @VisibleForTesting
        var ViewModelScopeContext = Dispatchers.IO
    }

    override val coroutineContext: CoroutineContext
        get() = Main

    suspend fun <T : Any> CoroutineScope.resultOf(task: suspend () -> T): Result<T> {
        return try {
            //network call should start on IO thread instead of default Main Thread
            OnSuccess(withContext(Dispatchers.IO) { task() })
        } catch (e: Exception) {
            if (e is CancellationException) {
                OnCancelException(e)
            } else OnError(e)
        }
    }
}
