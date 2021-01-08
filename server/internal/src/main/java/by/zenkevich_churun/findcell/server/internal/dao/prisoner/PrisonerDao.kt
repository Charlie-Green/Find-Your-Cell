package by.zenkevich_churun.findcell.server.internal.dao.prisoner

import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.PrisonerView
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


@org.springframework.stereotype.Repository
interface PrisonerDao: Repository<PrisonerEntity, Int> {

    @Query("select p from PrisonerView p where username=:username and pass=:passwordHash")
    fun get(
        username: String,
        passwordHash: ByteArray
    ): PrisonerView?

    @Query("select p from PrisonerEntity p where id=:id and pass=:passwordHash")
    fun get(
        id: Int,
        passwordHash: ByteArray
    ): PrisonerEntity?

    @Query("select count(*) from PrisonerEntity p where username=:username")
    fun countByUsername(username: String): Int

    fun save(prisoner: PrisonerEntity)
}