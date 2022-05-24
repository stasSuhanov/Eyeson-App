package com.example.eyesonapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eyesonapp.R
import com.example.eyesonapp.getAppComponent

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inject()
    }

    private fun inject() {
        getAppComponent().homeComponent()
            .create()
            .inject(this)
    }
}