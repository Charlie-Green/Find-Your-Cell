package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.common.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.util.ServerInternalUtil.optionalResult


class AuthorizationDao {
    // TODO: DI this.
    private val connection = DatabaseConnection()


    fun getPrisoner(
        username: String,
        passwordHash: ByteArray
    ): PrisonerEntity? {

        val man = connection.entityMan

        val prisonerClass = PrisonerEntity::class.java
        val q = man.createQuery(
            "select p from ${prisonerClass.simpleName} p where username=?0 and pass=?1",
            prisonerClass
        )
        q.setParameter(0, username)
        q.setParameter(1, passwordHash)

        return q.optionalResult
    }

    fun checkUsername(username: String): Boolean {
        val man = connection.entityMan

        val prisonerClass = PrisonerEntity::class.java
        val q = man.createQuery(
            "select count(*) from ${prisonerClass.simpleName} p where username=?0",
            java.lang.Long::class.java
        )
        q.setParameter(0, username)

        return q.singleResult >= 1
    }
}