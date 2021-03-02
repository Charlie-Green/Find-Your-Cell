package by.zenkevich_churun.findcell.server.internal.entity.key

import by.zenkevich_churun.findcell.domain.entity.Prisoner
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class ContactKey(
    @Column(name = "prisoner")
    var prisonerId: Int,

    @Column(name = "type")
    var typeOrdinal: Short
): Serializable {

    constructor(): this(Prisoner.INVALID_ID, 0)
}