package by.zenkevich_churun.findcell.server.internal.repo.jail

import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao


class JailsRepository(private val dao: JailsDao) {

    fun getJails(): List<Jail> {
        return dao.getJails()
    }
}