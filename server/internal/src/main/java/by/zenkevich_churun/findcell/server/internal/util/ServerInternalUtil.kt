package by.zenkevich_churun.findcell.server.internal.util

import javax.persistence.TypedQuery


object ServerInternalUtil {

    val <T> TypedQuery<T>.optionalResult: T?
        get() {
            val list = resultList
            return if(list.isEmpty()) null else list[0]
        }
}