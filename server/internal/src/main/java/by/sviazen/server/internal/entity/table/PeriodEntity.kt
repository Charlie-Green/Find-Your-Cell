package by.sviazen.server.internal.entity.table

import by.sviazen.domain.entity.Jail
import by.sviazen.server.internal.entity.key.PeriodKey
import javax.persistence.*


@Entity
@Table(name = "periods")
class PeriodEntity(

    @EmbeddedId
    var key: PeriodKey,

    @Column(name = "jail")
    var jailId: Int,

    @Column(name = "cell")
    var cellNumber: Short ) {


    constructor(): this(PeriodKey(), Jail.UNKNOWN_ID, -1)
}