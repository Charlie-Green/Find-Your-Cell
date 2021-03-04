package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.domain.entity.Jail
import by.zenkevich_churun.findcell.server.internal.entity.key.PeriodKey
import javax.persistence.*


@Entity
@Table(name = "Periods")
class PeriodEntity(

    @EmbeddedId
    var key: PeriodKey,

    @Column(name = "jail")
    var jailId: Int,

    @Column(name = "cell")
    var cellNumber: Short ) {


    constructor(): this(PeriodKey(), Jail.UNKNOWN_ID, -1)
}