package com.gpetuhov.android.samplecoroutines2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gpetuhov.android.samplecoroutines2.lifecycle.load
import com.gpetuhov.android.samplecoroutines2.lifecycle.then
import com.gpetuhov.android.samplecoroutines2.loader.Loader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // It is better to use dependency injection here
    private val loader = Loader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadButton.setOnClickListener {
            // And now, when we have all of our listeners, coroutines
            // and extension functions prepared,
            // we can load data like this.
            load { loader.getData() } then { textView.text = it }
        }
    }
}
