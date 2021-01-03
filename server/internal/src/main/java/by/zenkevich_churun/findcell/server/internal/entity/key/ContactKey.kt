package by.zenkevich_churun.findcell.server.internal.entity.key

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class ContactKey: Serializable {

    @Column(name = "prisoner")
    var prisonerId: Int = 0

    @Column(name = "type")
    var type: Short = 0


    override fun equals(other: Any?): Boolean {
        return (other is ContactKey) &&
            (prisonerId == other.prisonerId) &&
            (type == other.type)
    }

    override fun hashCode(): Int {
        return prisonerId.hashCode() xor type.hashCode()
    }
}