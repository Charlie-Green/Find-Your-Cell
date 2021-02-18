package by.zenkevich_churun.findcell.core.util.std


/** The Standard Library doesn't have an overload for [Short]s. **/
fun max(a: Short, b: Short): Short {
    return if(a > b) a else b
}


/** 0 is false, anything else is true. **/
fun Long.toBoolean(): Boolean
    = (this != 0L)

/** false is 0, true is 1. **/
fun Boolean.toLong(): Long
    = if(this) 1L else 0L