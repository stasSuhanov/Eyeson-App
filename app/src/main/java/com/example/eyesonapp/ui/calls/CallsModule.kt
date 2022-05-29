package com.example.eyesonapp.ui.calls

import androidx.lifecycle.MutableLiveData
import com.example.eyesonapp.ui.calls.chat.Message
import com.eyeson.sdk.events.CallRejectionReason
import com.eyeson.sdk.events.CallTerminationReason
import com.eyeson.sdk.events.EyesonEventListener
import com.eyeson.sdk.model.local.api.UserInfo
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
        return MutableLiveData(Message("", ""))
    }

    @ActivityRetainedScoped
    @Provides
    fun provideEyesonEventListener(
        meetingStateLiveData: MutableLiveData<MeetingState>,
        chatMessage: MutableLiveData<Message>
    ): EyesonEventListener {
        return object : EyesonEventListener() {

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
                chatMessage.postValue(Message(user.name, message))
            }
        }
    }
}