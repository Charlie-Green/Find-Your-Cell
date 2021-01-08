package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.sync.SynchronizationApi
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationScheduler
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import by.zenkevich_churun.findcell.remote.retrofit.sync.RetrofitSynchronizationApi
import by.zenkevich_churun.findcell.result.repo.sync.SynchronizationRepositoryImpl
import by.zenkevich_churun.findcell.result.sync.data.SynchronizedDataManagerImpl
import by.zenkevich_churun.findcell.result.sync.scheduler.SynchronizationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
interface ResultModule {

    @Binds
    fun synchronizationApi(
        impl: RetrofitSynchronizationApi
    ): SynchronizationApi

    @Binds
    fun synchronizedDataManager(
        impl: SynchronizedDataManagerImpl
    ): SynchronizedDataManager

    @Binds
    fun synchronizationScheduler(
        impl: SynchronizationSchedulerImpl
    ): SynchronizationScheduler

    @Binds
    fun synchronizationRepository(
        impl: SynchronizationRepositoryImpl
    ): SynchronizationRepository
}