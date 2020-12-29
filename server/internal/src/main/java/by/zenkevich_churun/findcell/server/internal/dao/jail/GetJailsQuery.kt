package by.zenkevich_churun.findcell.server.internal.dao.jail

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.JailEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class GetJailsQuery: LazyTypedQuery<JailEntity>(
    "select j from ${JailEntity::class.java.simpleName} j",
    JailEntity::class.java ) {

    override public fun getTypedQuery(  // Just make this method public.
        entityMan: EntityManager
    ): TypedQuery<JailEntity> = super.getTypedQuery(entityMan)
}