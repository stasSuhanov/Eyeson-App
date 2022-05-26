package com.example.eyesonapp.ui.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor() : ViewModel() {

    private val _isMicrophoneEnable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMicrophoneEnable: LiveData<Boolean> get() = _isMicrophoneEnable

    private val _isCameraEnable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isCameraEnable: LiveData<Boolean> get() = _isCameraEnable

    fun muteMicrophone() {
        _isMicrophoneEnable.value = false
        //TODO
    }

    fun unMuteMicrophone() {
        _isMicrophoneEnable.value = true
        //TODO
    }

    fun turnOffCamera() {
        _isCameraEnable.value = false
        //TODO
    }

    fun turnOnCamera() {
        _isCameraEnable.value = true
        //TODO
    }
}