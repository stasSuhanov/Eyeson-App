package com.example.eyesonapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eyesonapp.data.api.EyesonApi
import com.example.eyesonapp.di.BackgroundScheduler
import com.example.eyesonapp.di.UiScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: EyesonApi,
    @BackgroundScheduler private val backgroundScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    fun createMeetingRoom() {
        api.getLinks(API_KEY, USER).subscribeByDefault {
            Log.d("JSON",it.toString())
            //TODO
        }
    }

    private fun <T : Any> Single<T>.subscribeByDefault(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        this.subscribeOn(backgroundScheduler)
            .subscribeOn(uiScheduler)
            .subscribe(onSuccess, onError)
            .addTo(compositeDisposable)
    }

    companion object {
        private const val USER = "USER"
        private const val API_KEY = "sJrzTXWYn4HhehFY4LHqBzYI2lo9esPTtniViv93Xc"
    }
}