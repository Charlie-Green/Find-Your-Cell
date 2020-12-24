package by.zenkevich_churun.findcell.server.protocol.di

import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao
import by.zenkevich_churun.findcell.server.internal.dao.common.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
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
        }

        val authModule = module {
            single {
                AuthorizationRepository(get())
            }

            single {
                AuthorizationDao(get())
            }
        }

        val arestModule = module {
            // TODO
        }

        val schedModule = module {
            // TODO
        }

        val koinApp = startKoin {
            modules( listOf(
                commonModule,
                authModule,
                arestModule,
                schedModule
            ))
        }

        return koinApp.koin
    }
}