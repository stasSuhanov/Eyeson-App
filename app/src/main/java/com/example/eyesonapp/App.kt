package com.example.eyesonapp

import android.app.Application
import android.content.Context

class App : Application() {

    val appComponent = DaggerApplicationComponent.create()
}

fun Context.getAppComponent(): ApplicationComponent {
    return if (this is App) {
        this.appComponent
    } else {
        this.applicationContext.getAppComponent()
    }
}