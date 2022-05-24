package com.example.eyesonapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.eyesonapp.data.api.EyesonApi
import com.example.eyesonapp.di.BackgroundScheduler
import com.example.eyesonapp.di.UiScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: EyesonApi,
    @BackgroundScheduler backgroundScheduler: Scheduler,
    @UiScheduler uiScheduler: Scheduler,
) : ViewModel() {

    fun createMeetingRoom() {
        // TODO: Implement
    }
}