package com.example.eyesonapp.ui.calls

import androidx.lifecycle.MutableLiveData
import com.example.eyesonapp.ui.calls.chat.Message
import com.example.eyesonapp.utils.formatData
import com.eyeson.sdk.events.CallRejectionReason
import com.eyeson.sdk.events.CallTerminationReason
import com.eyeson.sdk.events.EyesonEventListener
import com.eyeson.sdk.model.local.api.UserInfo
import com.eyeson.sdk.model.local.meeting.BroadcastUpdate
import com.eyeson.sdk.model.local.meeting.Recording
import com.eyeson.sdk.model.local.meeting.SnapshotUpdate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.*

@InstallIn(ActivityRetainedComponent::class)
@Module
class CallsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMeetingStateLiveData(): MutableLiveData<MeetingState> {
        return MutableLiveData(MeetingState.CREATED)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideChatMessage(): MutableLiveData<Message> {
        return MutableLiveData<Message>()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideCurrentUserInfoLiveData(): MutableLiveData<UserInfo> {
        return MutableLiveData<UserInfo>()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideEyesonEventListener(
        meetingStateLiveData: MutableLiveData<MeetingState>,
        chatMessageLiveData: MutableLiveData<Message>,
        currentUserInfoLiveData: MutableLiveData<UserInfo>,
    ): EyesonEventListener {
        return object : EyesonEventListener() {

            override fun onMeetingJoining(
                name: String, startedAt: Date, user: UserInfo, locked: Boolean, guestToken: String,
                guestLink: String, activeRecording: Recording?, activeBroadcasts: BroadcastUpdate?,
                snapshots: SnapshotUpdate?
            ) {
                super.onMeetingJoining(name, startedAt, user, locked, guestToken, guestLink,
                    activeRecording, activeBroadcasts, snapshots)
                currentUserInfoLiveData.postValue(user)
            }

            override fun onMeetingJoined() {
                super.onMeetingJoined()
                meetingStateLiveData.postValue(MeetingState.JOINED)
            }

            override fun onMeetingJoinFailed(callRejectionReason: CallRejectionReason) {
                super.onMeetingJoinFailed(callRejectionReason)
                meetingStateLiveData.postValue(MeetingState.TERMINATED)
            }

            override fun onMeetingTerminated(callTerminationReason: CallTerminationReason) {
                super.onMeetingTerminated(callTerminationReason)
                meetingStateLiveData.postValue(MeetingState.TERMINATED)
            }

            override fun onChatMessageReceived(user: UserInfo, message: String, timestamp: Date) {
                super.onChatMessageReceived(user, message, timestamp)
                val isMine = user.id == currentUserInfoLiveData.value?.id
                val formattedData = formatData(timestamp)
                chatMessageLiveData.postValue(Message(user.name, message, formattedData,isMine))
            }
        }
    }
}