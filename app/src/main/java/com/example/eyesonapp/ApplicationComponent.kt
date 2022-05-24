package com.example.eyesonapp

import com.example.eyesonapp.data.api.NetworkModule
import com.example.eyesonapp.ui.calls.CallsSubcomponent
import com.example.eyesonapp.ui.home.HomeSubcomponent
import dagger.Component
import dagger.Module

@Component(modules = [
    ApplicationSubcomponetsModule::class,
    NetworkModule::class,
])
interface ApplicationComponent {

    fun homeComponent(): HomeSubcomponent.Factory
    fun callsComponent(): CallsSubcomponent.Factory
}

@Module(
    subcomponents = [
        HomeSubcomponent::class,
        CallsSubcomponent::class,
    ]
)
class ApplicationSubcomponetsModule