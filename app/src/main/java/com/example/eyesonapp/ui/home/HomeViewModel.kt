package com.example.eyesonapp.ui.home

import androidx.lifecycle.*
import com.example.eyesonapp.AppDestination
import com.example.eyesonapp.AppNavigator
import com.example.eyesonapp.data.api.EyesonApi
import com.example.eyesonapp.data.api.RoomResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: EyesonApi,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    val state: MutableLiveData<State> by lazy {
        MutableLiveData<State>(State.Initial)
    }

    // Key to manage created room as host
    private var roomAccessKey: String? = null

    // Link to join the meeting as guest
    private var guestInviteLink: String? = null

    fun createMeetingRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(State.Progress)
            var roomResponse: RoomResponse? = null
            try {
                roomResponse = api.getLinks(DEFAULT_USERNAME)
            } catch (e: Exception) {
                state.postValue(State.Initial)
                withContext(Dispatchers.Main) {
                    appNavigator.navigate(AppDestination.DefaultError)
                }
            }
            roomAccessKey = roomResponse?.accessKey
            roomResponse?.room?.guestToken?.let {
                guestInviteLink = "https://app.eyeson.team/?guest=$it"
                state.postValue(State.RoomCreated(guestInviteLink ?: ""))
            }
        }
    }

    fun sendInviteLink() {
        appNavigator.navigate(AppDestination.InviteLink(guestInviteLink ?: ""))
    }

    fun joinMeetingRoomAsHost() {
        appNavigator.navigate(AppDestination.JoinRoomAsHost(roomAccessKey ?: ""))
    }

    fun onBackPressed() {
        state.value = State.Initial
    }
}

sealed class State {
    object Initial: State()
    object Progress: State()
    data class RoomCreated(val guestJoinLink: String): State()
}

private const val DEFAULT_USERNAME = "DEFAULT_USERNAME"