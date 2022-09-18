package xyz.wim.dentrealitytask

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun <T> LiveData<T>.await(): T {
    return withContext(Dispatchers.Main.immediate) {
        suspendCancellableCoroutine { continuation ->
            val observer = object : Observer<T> {
                override fun onChanged(value: T) {
                    removeObserver(this)
                    continuation.resume(value) {
                        Timber.e(it)
                    }
                }
            }

            observeForever(observer)

            continuation.invokeOnCancellation {
                removeObserver(observer)
            }
        }
    }
}
