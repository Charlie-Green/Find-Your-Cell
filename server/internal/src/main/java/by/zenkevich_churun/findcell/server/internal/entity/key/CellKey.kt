package by.zenkevich_churun.findcell.server.internal.entity.key

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class CellKey: Serializable {

    @Column(name = "jail")
    var jailId: Int = 0

    @Column(name = "cell")
    var cellNumber: Short = 0
}