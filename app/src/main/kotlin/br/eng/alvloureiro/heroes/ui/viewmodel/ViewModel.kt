package br.eng.alvloureiro.heroes.ui.viewmodel

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred


interface ViewModel {
    fun executeOnUI(routine: suspend CoroutineScope.() -> Unit)

    fun executeOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        handleCancellationExceptionManually: Boolean = false)

    fun executeOnUITryCatchFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false)

    fun executeOnUITryFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        suppressCancellationException: Boolean = false)

    fun cancelAllCoroutines()

    suspend fun <T> async(routine: suspend CoroutineScope.() -> T): Deferred<T>

    suspend fun <T> asyncAwait(routine: suspend CoroutineScope.() -> T): T

    fun cancelAllAsync()

    fun cleanup()
}
