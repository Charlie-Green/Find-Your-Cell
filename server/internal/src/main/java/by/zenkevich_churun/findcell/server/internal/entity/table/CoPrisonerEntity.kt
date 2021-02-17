package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.server.internal.entity.key.CoPrisonerKey
import javax.persistence.*


@Entity
@Table(name = "PrisonersRelations")
class CoPrisonerEntity {

    @EmbeddedId
    lateinit var key: CoPrisonerKey

    @Column(name = "rel")
    var relationOrdinal: Short = -1

    @Column(name = "jail")
    var commonJailId: Int = 0

    @Column(name = "cell")
    var commonCellNumber: Short = 0


    companion object {
        /** This is to be used instead or [CoPrisoner.Relation.ordinal] to specify
          * such pair (p1; p2) that p2 has declined request from p1.
          * Such relation is shown as [CoPrisoner.Relation.OUTCOMING_REQUEST] to p1
          * or as [CoPrisoner.Relation.REQUEST_DECLINED] for p2. **/
        const val RELATION_ORDINAL_OUTCOMING_DECLINED: Short = 64
    }
}