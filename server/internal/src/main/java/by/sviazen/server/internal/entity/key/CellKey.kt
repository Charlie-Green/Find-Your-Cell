package by.sviazen.server.internal.entity.key

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class CellKey: Serializable {

    @Column(name = "jail")
    var jailId: Int = 0

    @Column(name = "number")
    var cellNumber: Short = 0
}