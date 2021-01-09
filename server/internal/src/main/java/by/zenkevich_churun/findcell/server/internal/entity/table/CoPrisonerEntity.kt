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


    var relation: CoPrisoner.Relation
        get() { return CoPrisoner.Relation.values()[relationOrdinal.toInt()] }
        set(value) { relationOrdinal = value.ordinal.toShort() }
}