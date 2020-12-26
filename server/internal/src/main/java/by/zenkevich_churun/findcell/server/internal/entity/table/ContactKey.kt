package by.zenkevich_churun.findcell.server.internal.entity.table

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


/** Composite primary key for [ContactEntity]. **/
@Embeddable
class ContactKey: Serializable {

    @Column(name = "prisoner")
    var prisonerId: Int = 0

    @Column(name = "type")
    var type: Short = 0

}