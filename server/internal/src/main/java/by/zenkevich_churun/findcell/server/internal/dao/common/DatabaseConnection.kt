package by.zenkevich_churun.findcell.server.internal.dao.common

import javax.persistence.*


class DatabaseConnection {

    val entityMan by lazy {
        Persistence
            .createEntityManagerFactory("FindCell")
            .createEntityManager()
    }


    inline fun withTransaction(action: (entityMan: EntityManager) -> Unit) {
        val t = entityMan.transaction

        t.begin()
        try {
            action(entityMan)
        } finally {
            t.commit()
        }
    }
}