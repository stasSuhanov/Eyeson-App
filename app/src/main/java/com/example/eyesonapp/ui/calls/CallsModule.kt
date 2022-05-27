package com.example.eyesonapp.ui.calls

import androidx.lifecycle.MutableLiveData
import com.eyeson.sdk.events.CallRejectionReason
import com.eyeson.sdk.events.CallTerminationReason
import com.eyeson.sdk.events.EyesonEventListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

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
    fun provideEyesonEventListener(meetingStateLiveData: MutableLiveData<MeetingState>): EyesonEventListener {
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
        }
    }
}