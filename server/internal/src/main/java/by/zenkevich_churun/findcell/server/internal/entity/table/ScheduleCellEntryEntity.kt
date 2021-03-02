package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.server.internal.entity.key.ScheduleCellEntryKey
import javax.persistence.*


/** M-M relationship table between [CellEntity] and [ArestEntity].
  * Represents the fact that a [Cell] is to be displayed on the [Arest]'s [Schedule] UI.
  * In other words, the user may admit they have been imprisoned
  * into this [Cell] some time during this [Arest] **/
@Entity
@Table(name = "ScheduleCellEntries")
class ScheduleCellEntryEntity(

    @EmbeddedId
    var key: ScheduleCellEntryKey ) {

    constructor(): this(ScheduleCellEntryKey())
}