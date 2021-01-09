package by.zenkevich_churun.findcell.core.injected.db

import by.zenkevich_churun.findcell.entity.pojo.FullJailPojo


interface JailsCache {
    fun cache(jails: List<FullJailPojo>)
}