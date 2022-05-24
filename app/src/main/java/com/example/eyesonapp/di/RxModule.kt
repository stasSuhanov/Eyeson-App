package com.example.eyesonapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RxModule {

    @Singleton
    @BackgroundScheduler
    @Provides
    fun provideDefaultBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Singleton
    @UiScheduler
    @Provides
    fun provideDefaultUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UiScheduler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BackgroundScheduler