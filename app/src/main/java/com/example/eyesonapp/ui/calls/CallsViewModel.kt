package com.example.eyesonapp.ui.calls

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eyesonapp.AppDestination
import com.example.eyesonapp.AppNavigator
import com.example.eyesonapp.R
import com.example.eyesonapp.ui.calls.chat.Message
import com.eyeson.sdk.EyesonMeeting
import com.eyeson.sdk.events.CallTerminationReason
import com.eyeson.sdk.events.EyesonEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.webrtc.EglBase
import org.webrtc.VideoSink
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    application: Application,
    val chatMessage: MutableLiveData<Message>,
    val meetingStateLiveData: MutableLiveData<MeetingState>,
    private val appNavigator: AppNavigator,
    private val eyesonEventListener: EyesonEventListener,
) : AndroidViewModel(application) {

    private var eyesonMeeting: EyesonMeeting? = null

    private val defaultUiState by lazy {
        UiState(
            audioMuted = false,
            videoDisabled = false,
            defaultAspectRatio = true
        )
    }
    private val _uiState: MutableLiveData<UiState> = MutableLiveData(defaultUiState)
    val uiState: LiveData<UiState> get() = _uiState

    fun connectToRoom(
        roomAccessKey: String?,
        guestToken: String?,
        local: VideoSink?,
        remote: VideoSink?
    ) {
        if (eyesonMeeting != null) return

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
        _uiState.postValue(_uiState.value?.copy(audioMuted = mute) ?: defaultUiState)
        eyesonMeeting?.setMicrophoneEnabled(!mute)
    }

    fun muteCamera(mute: Boolean) {
        _uiState.postValue(_uiState.value?.copy(videoDisabled = mute) ?: defaultUiState)
        eyesonMeeting?.setVideoEnabled(!mute)
    }

    fun switchAspectRatio(defaultAspectRatio: Boolean) {
        _uiState.postValue(_uiState.value
            ?.copy(defaultAspectRatio = defaultAspectRatio) ?: defaultUiState)
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            eyesonMeeting?.sendChatMessage(message)
        }
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

    fun onMeetingTerminated(reason: CallTerminationReason) {
        if (reason == CallTerminationReason.OK) {
            appNavigator.navigate(AppDestination.FinishCurrentActivity)
        } else {
            appNavigator.navigate(AppDestination.ShowErrorMessageAndFinishFlow(
                title = R.string.calls_screen_error_connection_terminated_title,
                message = R.string.calls_screen_error_connection_terminated_message,
            ))
        }
    }

    fun disconnect() {
        eyesonMeeting?.leave()
        meetingStateLiveData.postValue(MeetingState.TERMINATED(reason = CallTerminationReason.OK))
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

data class UiState(
    val audioMuted: Boolean,
    val videoDisabled: Boolean,
    val defaultAspectRatio: Boolean,
)

sealed class MeetingState {
    object CREATED: MeetingState()
    object JOINED: MeetingState()
    data class TERMINATED(val reason: CallTerminationReason): MeetingState()
}

const val DEFAULT_GUEST_NAME = "Guest"