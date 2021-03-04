package by.sviazen.app.di

import by.sviazen.core.api.arest.ArestsApi
import by.sviazen.core.api.jail.JailsApi
import by.sviazen.core.api.auth.ProfileApi
import by.sviazen.core.api.sched.ScheduleApi
import by.sviazen.core.api.sched.SchedulePropertiesAccessor
import by.sviazen.core.injected.db.JailsCache
import by.sviazen.prisoner.db.cache.JailsCacheImpl
import by.sviazen.prisoner.repo.sched.map.SchedulePropertiesAccessorImpl
import by.sviazen.remote.retrofit.arest.RetrofitArestsApi
import by.sviazen.remote.retrofit.jail.RetrofitJailsApi
import by.sviazen.remote.retrofit.profile.RetrofitProfileApi
import by.sviazen.remote.retrofit.sched.RetrofitScheduleApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
interface PrisonerModule {

    @Singleton
    @Binds
    fun prisonerApi(
        impl: RetrofitProfileApi
    ): ProfileApi

    @Singleton
    @Binds
    fun scheduleApi(
        impl: RetrofitScheduleApi
    ): ScheduleApi

    @Singleton
    @Binds
    fun jailsApi(
        impl: RetrofitJailsApi
    ): JailsApi

    @Singleton
    @Binds
    fun arestsApi(
        impl: RetrofitArestsApi
    ): ArestsApi

    @Singleton
    @Binds
    fun schedulePropertiesAccessor(
        impl: SchedulePropertiesAccessorImpl
    ): SchedulePropertiesAccessor

    @Binds
    fun jailsCache(
        impl: JailsCacheImpl
    ): JailsCache
}