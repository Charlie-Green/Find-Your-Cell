package by.sviazen.server.internal.entity.key

import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Jail
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class ScheduleCellEntryKey(

    @Column(name = "arest")
    var arestId: Int,

    @Column(name = "jail")
    var jailId: Int,

    @Column(name = "cell")
    var cellNumber: Short

): Serializable {


    constructor(): this(Arest.INVALID_ID, Jail.UNKNOWN_ID, 0)
}