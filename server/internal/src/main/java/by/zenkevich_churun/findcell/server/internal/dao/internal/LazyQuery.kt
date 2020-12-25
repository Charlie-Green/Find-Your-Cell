package by.zenkevich_churun.findcell.server.internal.dao.internal

import javax.persistence.EntityManager
import javax.persistence.Query


abstract class LazyQuery(private val sql: String) {

    private var q: Query? = null


    fun getQuery(entityMan: EntityManager): Query {
        return q ?: synchronized(this) {
            q ?: entityMan.createQuery(sql).also {
                q = it
            }
        }
    }
}