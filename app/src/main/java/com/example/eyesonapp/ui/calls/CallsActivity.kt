package com.example.eyesonapp.ui.calls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eyesonapp.R
import com.example.eyesonapp.getAppComponent

class CallsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calls)
        inject()
    }

    private fun inject() {
        getAppComponent().callsComponent()
            .create()
            .inject(this)
    }
}