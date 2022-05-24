package com.example.eyesonapp.ui.calls

import dagger.Subcomponent

@Subcomponent
interface CallsSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CallsSubcomponent
    }

    fun inject(callsActivity: CallsActivity)
}