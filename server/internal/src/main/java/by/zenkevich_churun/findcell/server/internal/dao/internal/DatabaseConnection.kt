package by.zenkevich_churun.findcell.server.internal.dao.internal

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
            t.commit()
        } catch(thr: Throwable) {
            t.rollback()
            throw thr
        } finally {
            entityMan.clear()
        }
    }
}