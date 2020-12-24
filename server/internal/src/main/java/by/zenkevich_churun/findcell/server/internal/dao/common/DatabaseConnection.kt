package by.zenkevich_churun.findcell.server.internal.dao.common

import javax.persistence.Persistence


// @Singleton
class DatabaseConnection /* @Inject constructor() */ {

    val entityMan by lazy {
        Persistence
            .createEntityManagerFactory("FindCell")
            .createEntityManager()
    }
}