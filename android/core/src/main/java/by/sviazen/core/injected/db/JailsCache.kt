package by.sviazen.core.injected.db

import by.sviazen.domain.contract.jail.FullJailPojo


interface JailsCache {
    fun cache(jails: List<FullJailPojo>)
}