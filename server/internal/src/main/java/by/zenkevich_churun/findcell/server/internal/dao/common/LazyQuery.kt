package by.zenkevich_churun.findcell.server.internal.dao.common

import javax.persistence.EntityManager
import javax.persistence.TypedQuery


/** Allows caching [TypedQuery] objects without code duplication. **/
abstract class LazyQuery<T>(
    private val sql: String,
    private val klass: Class<T> ) {

    private var q: TypedQuery<T>? = null


    protected open fun getTypedQuery(entityMan: EntityManager): TypedQuery<T> {
        return q ?: synchronized(this) {
            q ?: createQuery(entityMan).also {
                q = it
            }
        }
    }


    private fun createQuery(entityMan: EntityManager): TypedQuery<T>
        = entityMan.createQuery(sql, klass)
}