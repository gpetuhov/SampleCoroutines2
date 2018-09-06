package com.gpetuhov.android.samplecoroutines2.loader

// This is our long-running operation
// It "loads" data and returns result
class Loader {
    fun getData(): String {
        Thread.sleep(5000)
        return "Load result"
    }
}