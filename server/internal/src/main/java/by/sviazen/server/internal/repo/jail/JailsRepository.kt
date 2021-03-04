package by.sviazen.server.internal.repo.jail

import by.sviazen.domain.contract.jail.JailsListPojo
import by.sviazen.domain.contract.jail.SeatCountsPojo
import by.sviazen.server.internal.dao.jail.JailsDao
import by.sviazen.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class JailsRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var jailsDao: JailsDao


    fun getJails(): JailsListPojo {
        val jails = jailsDao.get()
        return JailsListPojo.from(jails)
    }

    fun getSeatCounts(jailId: Int): SeatCountsPojo {
        val counts = jailsDao.getSeatCounts(jailId)
        return SeatCountsPojo(counts)
    }
}