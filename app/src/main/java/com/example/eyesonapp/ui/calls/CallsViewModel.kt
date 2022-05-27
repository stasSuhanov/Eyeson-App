package com.example.eyesonapp.ui.calls

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eyesonapp.AppDestination
import com.example.eyesonapp.AppNavigator
import com.eyeson.sdk.EyesonMeeting
import com.eyeson.sdk.events.EyesonEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.webrtc.EglBase
import org.webrtc.VideoSink
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    application: Application,
    val meetingStateLiveData: MutableLiveData<MeetingState>,
    private val appNavigator: AppNavigator,
    private val eyesonEventListener: EyesonEventListener,
) : AndroidViewModel(application) {

    private var eyesonMeeting: EyesonMeeting? = null

    private val _isMicrophoneEnable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMicrophoneEnable: LiveData<Boolean> get() = _isMicrophoneEnable

    private val _isCameraEnable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isCameraEnable: LiveData<Boolean> get() = _isCameraEnable

    fun connectToRoom(
        roomAccessKey: String?,
        guestToken: String?,
        local: VideoSink?,
        remote: VideoSink?
    ) {
        when {
            !roomAccessKey.isNullOrBlank() -> {
                connect(roomAccessKey, local, remote)
            }
            !guestToken.isNullOrBlank() -> {
                connectAsGuest(guestToken, local, remote)
            }
            else -> {
                appNavigator.navigate(AppDestination.DefaultError)
            }
        }
    }

    fun muteMicrophone(mute: Boolean) {
        _isMicrophoneEnable.value = mute
        eyesonMeeting?.setMicrophoneEnabled(mute)
    }

    fun muteCamera(mute: Boolean) {
        _isCameraEnable.value = mute
        eyesonMeeting?.setVideoEnabled(mute)
    }

    fun sendMessage(message: String) {
        eyesonMeeting?.sendChatMessage(message)
    }

    private fun connect(accessKey: String, local: VideoSink?, remote: VideoSink?) {
        viewModelScope.launch {
            eyesonMeeting = EyesonMeeting(
                eventListener = eyesonEventListener,
                application = getApplication()
            ).apply {
                join(
                    accessKey = accessKey,
                    frontCamera = true,
                    audiOnly = false,
                    local = local,
                    remote = remote,
                    microphoneEnabledOnStart = true,
                    videoEnabledOnStart = true
                )
            }
        }
    }

    private fun connectAsGuest(
        guestToken: String,
        local: VideoSink?,
        remote: VideoSink?
    ) {
        viewModelScope.launch {
            eyesonMeeting = EyesonMeeting(
                eventListener = eyesonEventListener,
                application = getApplication()
            ).apply {
                joinAsGuest(
                    guestToken = guestToken,
                    name = DEFAULT_GUEST_NAME,
                    id = null,
                    avatar = null,
                    frontCamera = true,
                    audiOnly = false,
                    local = local,
                    remote = remote,
                    microphoneEnabledOnStart = true,
                    videoEnabledOnStart = true
                )
            }
        }
    }

    fun disconnect() {
        eyesonMeeting?.leave()
        meetingStateLiveData.postValue(MeetingState.TERMINATED)
    }

    fun getEglContext(): EglBase.Context? {
        return eyesonMeeting?.getEglContext()
    }

    fun clearTarget() {
        eyesonMeeting?.setLocalVideoTarget(null)
        eyesonMeeting?.setRemoteVideoTarget(null)
    }

    fun setTargets(local: VideoSink?, remote: VideoSink?) {
        eyesonMeeting?.setLocalVideoTarget(local)
        eyesonMeeting?.setRemoteVideoTarget(remote)
    }
}

enum class MeetingState {
    CREATED, JOINED, TERMINATED
}

const val DEFAULT_GUEST_NAME = "GUEST"