package by.sviazen.remote.di

import by.sviazen.core.api.arest.ArestsApi
import by.sviazen.core.api.auth.ProfileApi
import by.sviazen.core.api.cp.CoPrisonersApi
import by.sviazen.core.api.jail.JailsApi
import by.sviazen.core.api.sched.ScheduleApi
import by.sviazen.core.api.sync.SynchronizationApi
import by.sviazen.remote.retrofit.arest.RetrofitArestsApi
import by.sviazen.remote.retrofit.cp.RetrofitCoPrisonersApi
import by.sviazen.remote.retrofit.jail.RetrofitJailsApi
import by.sviazen.remote.retrofit.profile.RetrofitProfileApi
import by.sviazen.remote.retrofit.sched.RetrofitScheduleApi
import by.sviazen.remote.retrofit.sync.RetrofitSynchronizationApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
interface RemoteModule {

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


    @Binds
    fun synchronizationApi(
        impl: RetrofitSynchronizationApi
    ): SynchronizationApi

    @Binds
    fun coPrisonersApi(
        impl: RetrofitCoPrisonersApi
    ): CoPrisonersApi
}