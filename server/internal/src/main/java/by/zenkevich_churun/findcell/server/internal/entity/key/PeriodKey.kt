package by.zenkevich_churun.findcell.server.internal.entity.key

import by.zenkevich_churun.findcell.domain.entity.Arest
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class PeriodKey(
    @Column(name = "arest")
    var arestId: Int,

    @Column(name = "start")
    var start: Long,

    @Column(name = "end")
    var end: Long
): Serializable {

    constructor(): this(Arest.INVALID_ID, 0L, 0L)


    override fun equals(other: Any?): Boolean {
        return (other is PeriodKey) &&
            (arestId == other.arestId) &&
            (start == other.start) &&
            (end == other.end)
    }

    override fun hashCode(): Int {
        return arestId.hashCode() xor
            start.hashCode() xor
            end.hashCode()
    }
}