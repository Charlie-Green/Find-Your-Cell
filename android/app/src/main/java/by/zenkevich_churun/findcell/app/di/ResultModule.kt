package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.core.api.sync.SynchronizationApi
import by.zenkevich_churun.findcell.core.injected.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.core.injected.sync.AutomaticSyncManager
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationScheduler
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import by.zenkevich_churun.findcell.remote.retrofit.cp.RetrofitCoPrisonersApi
import by.zenkevich_churun.findcell.remote.retrofit.sync.RetrofitSynchronizationApi
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepositoryImpl
import by.zenkevich_churun.findcell.result.repo.sync.AutomaticSyncManagerImpl
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
    fun syncFlagHolder(
        impl: AutomaticSyncManagerImpl
    ): AutomaticSyncManager

    @Binds
    fun synchronizationRepository(
        impl: SynchronizationRepositoryImpl
    ): SynchronizationRepository

    @Binds
    fun coPrisonersApi(
        impl: RetrofitCoPrisonersApi
    ): CoPrisonersApi

    @Binds
    fun coPrisonersRepository(
        impl: CoPrisonersRepositoryImpl
    ): CoPrisonersRepository
}