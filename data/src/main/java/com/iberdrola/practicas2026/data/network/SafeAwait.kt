package com.iberdrola.practicas2026.data.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Alternativa a Retromock `await` que no crashea al cancelar la coroutine.
 * Comprueba `cont.isActive` antes de resumir, evitando el "Already resumed".
 */
suspend fun <T> Call<T>.safeAwait(): T = suspendCancellableCoroutine { cont ->
    cont.invokeOnCancellation { cancel() }
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!cont.isActive) return
            val body = response.body()
            if (response.isSuccessful && body != null) {
                cont.resume(body)
            } else {
                cont.resumeWithException(IOException("HTTP ${response.code()}"))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!cont.isActive) return
            cont.resumeWithException(t)
        }
    })
}
