package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.server.internal.entity.key.CellScheduleEntryKey
import javax.persistence.*


/** M-M relationship table between [CellEntity] and [ArestEntity].
  * Represents the fact that a [Cell] is to be displayed on the [Arest]'s [Schedule] UI.
  * In other words, the user may admit they have been imprisoned
  * into this [Cell] some time during this [Arest] **/
@Entity
@Table(name = "CellScheduleEntries")
class CellScheduleEntryEntity {

    @EmbeddedId
    var key: CellScheduleEntryKey? = null
}