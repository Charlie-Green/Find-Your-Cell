package by.zenkevich_churun.findcell.core.util.std


/** The Standard Library doesn't have an overload for [Short]s. **/
fun max(a: Short, b: Short): Short {
    return if(a > b) a else b
}