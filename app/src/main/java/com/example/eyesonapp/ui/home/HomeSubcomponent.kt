package com.example.eyesonapp.ui.home

import dagger.Subcomponent

@Subcomponent
interface HomeSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeSubcomponent
    }

    fun inject(homeActivity: HomeActivity)
}