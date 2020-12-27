package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.core.api.auth.ProfileApi
import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.remote.retrofit.arest.RetrofitArestsApi
import by.zenkevich_churun.findcell.remote.retrofit.jail.RetrofitJailsApi
import by.zenkevich_churun.findcell.remote.retrofit.profile.RetrofitProfileApi
import by.zenkevich_churun.findcell.remote.retrofit.sched.RetrofitScheduleApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
interface ApplicationModule {

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
}