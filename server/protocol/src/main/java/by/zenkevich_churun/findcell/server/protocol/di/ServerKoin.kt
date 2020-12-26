package by.zenkevich_churun.findcell.server.protocol.di

import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao
import by.zenkevich_churun.findcell.server.internal.dao.common.CommonDao
import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.dao.profile.ProfileDao
import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import by.zenkevich_churun.findcell.server.internal.repo.profile.ProfileRepository
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.dsl.module


object ServerKoin {

    private var k: Koin? = null


    fun instance(): Koin {
        return k ?: synchronized(this) {
            k ?: createKoin().also {
                k = it
            }
        }
    }

    private fun createKoin(): Koin {
        val commonModule = module {
            single {
                DatabaseConnection()
            }

            single {
                CommonDao(get())
            }
        }

        val authModule = module {
            single {
                AuthorizationRepository(get())
            }

            single {
                AuthorizationDao(get())
            }
        }

        val profileModule = module {
            single {
                ProfileRepository(get(), get())
            }

            single {
                ProfileDao(get())
            }
        }

        val arestModule = module {
            single {
                ArestsDao(get())
            }

            single {
                ArestsRepository(get(), get())
            }
        }

        val schedModule = module {
            // TODO
        }

        val koinApp = startKoin {
            modules( listOf(
                commonModule,
                authModule,
                profileModule,
                arestModule,
                schedModule
            ))
        }

        return koinApp.koin
    }
}