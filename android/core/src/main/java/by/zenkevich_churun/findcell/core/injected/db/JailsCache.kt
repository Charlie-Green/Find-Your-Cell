package by.zenkevich_churun.findcell.core.injected.db

import by.zenkevich_churun.findcell.domain.contract.jail.FullJailPojo


interface JailsCache {
    fun cache(jails: List<FullJailPojo>)
}