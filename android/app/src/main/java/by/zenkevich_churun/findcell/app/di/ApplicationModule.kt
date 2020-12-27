package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.core.api.auth.ProfileApi
import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.prisoner.api.ram.arest.RamArestsApi
import by.zenkevich_churun.findcell.prisoner.api.ram.jail.RamJailsApi
import by.zenkevich_churun.findcell.prisoner.api.ram.sched.RamScheduleApi
import by.zenkevich_churun.findcell.remote.lan.auth.RetrofitProfileApi
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
        impl: RamScheduleApi
    ): ScheduleApi

    @Singleton
    @Binds
    fun jailsApi(
        impl: RamJailsApi
    ): JailsApi

    @Singleton
    @Binds
    fun arestsApi(
        impl: RamArestsApi
    ): ArestsApi
}