package com.example.eyesonapp.di

import android.content.Context
import com.example.eyesonapp.AppActivityManager
import com.example.eyesonapp.AppActivityManagerImpl
import com.example.eyesonapp.AppNavigator
import com.example.eyesonapp.AppNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NavigationModule {

    @Singleton
    @Provides
    fun provideAppNavigator(appActivityManager: AppActivityManager): AppNavigator {
        return AppNavigatorImpl(appActivityManager)
    }

    @Singleton
    @Provides
    fun provideAppActivityManager(@ApplicationContext applicationContext: Context): AppActivityManager {
        return AppActivityManagerImpl(applicationContext)
    }
}