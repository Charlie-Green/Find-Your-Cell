package by.sviazen.server.internal.entity.key

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class CoPrisonerKey: Serializable {

    @Column(name = "p1")
    var id1: Int = 0

    @Column(name = "p2")
    var id2: Int = 0
}