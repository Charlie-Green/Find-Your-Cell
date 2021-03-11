package by.sviazen.server.internal.entity.table

import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Cell
import by.sviazen.server.internal.entity.key.ScheduleCellEntryKey
import javax.persistence.*


/** M-M relationship table between [CellEntity] and [ArestEntity].
  * Represents the fact that a [Cell] is to be displayed on the [Arest]'s [Schedule] UI.
  * In other words, the user may admit they have been imprisoned
  * into this [Cell] some time during this [Arest] **/
@Entity
@Table(name = "schedule_cell_entries")
class ScheduleCellEntryEntity(

    @EmbeddedId
    var key: ScheduleCellEntryKey ) {

    constructor(): this(ScheduleCellEntryKey())
}