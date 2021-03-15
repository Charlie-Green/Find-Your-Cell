package by.sviazen.prisoner.di

import by.sviazen.core.api.sched.SchedulePropertiesAccessor
import by.sviazen.core.injected.db.JailsCache
import by.sviazen.core.repo.arest.ArestsRepository
import by.sviazen.core.repo.jail.JailsRepository
import by.sviazen.core.repo.profile.ProfileRepository
import by.sviazen.core.repo.sched.ScheduleRepository
import by.sviazen.prisoner.db.cache.JailsCacheImpl
import by.sviazen.prisoner.repo.arest.ArestsRepositoryImpl
import by.sviazen.prisoner.repo.jail.JailsRepositoryImpl
import by.sviazen.prisoner.repo.profile.ProfileRepositoryImpl
import by.sviazen.prisoner.repo.sched.SchedulePropertiesAccessorImpl
import by.sviazen.prisoner.repo.sched.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
interface PrisonerModule {

    @Singleton
    @Binds
    fun profileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Singleton
    @Binds
    fun jailsRepository(
        impl: JailsRepositoryImpl
    ): JailsRepository

    @Singleton
    @Binds
    fun arestsRepository(
        impl: ArestsRepositoryImpl
    ): ArestsRepository

    @Singleton
    @Binds
    fun scheduleRepository(
        impl: ScheduleRepositoryImpl
    ): ScheduleRepository


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