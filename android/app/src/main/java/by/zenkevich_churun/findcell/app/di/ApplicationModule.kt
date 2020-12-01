package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.JailsApi
import by.zenkevich_churun.findcell.core.api.ProfileApi
import by.zenkevich_churun.findcell.core.api.ScheduleApi
import by.zenkevich_churun.findcell.prisoner.api.ram.jail.RamJailsApi
import by.zenkevich_churun.findcell.prisoner.api.ram.profile.RamProfileApi
import by.zenkevich_churun.findcell.prisoner.api.ram.sched.RamScheduleApi
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
        impl: RamProfileApi
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
}