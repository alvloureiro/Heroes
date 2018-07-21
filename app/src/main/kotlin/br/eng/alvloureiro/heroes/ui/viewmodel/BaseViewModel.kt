package br.eng.alvloureiro.heroes.ui.viewmodel

import br.eng.alvloureiro.heroes.extensions.tryCatch
import br.eng.alvloureiro.heroes.extensions.tryCatchFinally
import br.eng.alvloureiro.heroes.extensions.tryFinally
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI


abstract class BaseViewModel: ViewModel {

    private val mJobs: MutableList<Job> = mutableListOf()
    private val mDeferreds: MutableList<Deferred<*>> = mutableListOf()


    @Synchronized
    override fun executeOnUI(routine: suspend CoroutineScope.() -> Unit) {
        val job = launch(UI) { routine() }
        mJobs.add(job)
        job.invokeOnCompletion { mJobs.remove(job) }
    }

    @Synchronized
    override fun executeOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        handleCancellationExceptionManually: Boolean)
    {
        executeOnUI {
            tryCatch(
                tryBlock,
                catchBlock,
                handleCancellationExceptionManually
            )
        }
    }

    @Synchronized
    override fun executeOnUITryCatchFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean)
    {
        executeOnUI {
            tryCatchFinally(
                tryBlock,
                catchBlock,
                finallyBlock,
                handleCancellationExceptionManually
            )
        }
    }

    @Synchronized
    override fun executeOnUITryFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        suppressCancellationException: Boolean)
    {
        executeOnUI {
            tryFinally(tryBlock, finallyBlock, suppressCancellationException)
        }
    }

    @Synchronized
    override fun cancelAllCoroutines() {
        val size = mJobs.size

        if (size > 0) {
            for (i in size - 1 downTo 0) {
                mJobs[i].cancel()
            }
        }
    }

    @Synchronized
    override suspend fun <T> async(routine: suspend CoroutineScope.() -> T): Deferred<T> {
        val deferred: Deferred<T> = async(CommonPool) { routine() }
        mDeferreds.add(deferred)
        deferred.invokeOnCompletion { mDeferreds.remove(deferred) }
        return deferred
    }

    @Synchronized
    override suspend fun <T> asyncAwait(routine: suspend CoroutineScope.() -> T): T {
        return async(routine).await()
    }

    @Synchronized
    override fun cancelAllAsync() {
        val deferredObjectsSize = mDeferreds.size

        if (mDeferreds.size > 0) {
            for (i in deferredObjectsSize - 1 downTo 0) {
                mDeferreds[i].cancel()
            }
        }
    }

    @Synchronized
    override fun cleanup() {
        cancelAllAsync()
    }
}
