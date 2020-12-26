package by.zenkevich_churun.findcell.server.internal.entity.key

import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


/** Composite primary key for [PeriodEntity]. **/
@Embeddable
class PeriodKey: Serializable {

    @Column(name = "arest")
    var arestId: Int = 0

    @Column(name = "start")
    var start: Long = 0L

    @Column(name = "end")
    var end: Long = 0L
}