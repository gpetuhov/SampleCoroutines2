package com.gpetuhov.android.samplecoroutines2.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

// This one is needed to cancel our long-running operation if activity is destroyed
class CoroutineLifecycleListener(private val deferred: Deferred<*>) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelCoroutine() {
        if (!deferred.isCancelled) {
            deferred.cancel()
        }
    }
}

// Here we declare an extension function for a LifececleOwnwer
// (which is our MainActivity).
// It takes our long-running operation as a lambda.
fun <T> LifecycleOwner.load(loader: () -> T): Deferred<T> {
    // This async will start only after start() or await() is called
    val deferred = async(start = CoroutineStart.LAZY) {
        loader()
    }

    // Register our observer to observe the lifecycle,
    // so that the Deferred for our long-running operation
    // will be canceled if MainActivity is destroyed.
    lifecycle.addObserver(CoroutineLifecycleListener(deferred))
    return deferred
}

// This extension function takes in a lambda with instructions,
// what to do with the results of our long-running operation.
// The function is marked with infix keyword for the simplicity of usage (see MainActivity).
infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
    // Here we launch coroutine on the main thread
    return launch(UI) {
        // This will start our long-running operation.
        // The coroutine will be suspended, but the main thread will NOT.
        // Here this keyword references the coroutine,
        // so to reference Deferred itself we have to use this@then.
        block(this@then.await())
    }
}


