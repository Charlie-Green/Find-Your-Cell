package by.sviazen.server.internal.entity.table

import by.sviazen.server.internal.entity.key.CellKey
import javax.persistence.*


@Entity
@Table(name = "Cells")
class CellEntity {

    @EmbeddedId
    lateinit var key: CellKey

    @Column(name = "seats")
    var seatCount: Short = 0
}