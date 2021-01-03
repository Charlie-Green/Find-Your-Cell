package by.zenkevich_churun.findcell.server.internal.repo.jail

import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class JailsRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var jailsDao: JailsDao


    fun getJails(): List<Jail> {
        return jailsDao.get()
    }
}