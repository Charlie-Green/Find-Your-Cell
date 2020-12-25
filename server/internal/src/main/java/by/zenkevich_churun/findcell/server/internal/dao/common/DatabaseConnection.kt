package by.zenkevich_churun.findcell.server.internal.dao.common

import javax.persistence.*


// @Singleton
class DatabaseConnection /* @Inject constructor() */ {

    val entityMan by lazy {
        Persistence
            .createEntityManagerFactory("FindCell")
            .createEntityManager()
    }


    inline fun withTransaction(action: (entityMan: EntityManager) -> Unit) {
        val t = entityMan.transaction

        t.begin()
        action(entityMan)
        t.commit()
    }
}