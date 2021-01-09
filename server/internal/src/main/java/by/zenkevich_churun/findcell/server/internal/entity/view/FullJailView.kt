package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.entity.pojo.FullJailPojo
import by.zenkevich_churun.findcell.server.internal.entity.table.CellEntity
import javax.persistence.*


@Entity
@Table(name = "Jails")
class FullJailView: FullJailPojo() {

    private var seatCounts: List<Short>? = null


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int = Jail.UNKNOWN_ID

    @Column(name = "name")
    override var name: String = ""

    @OneToMany(targetEntity = CellEntity::class)
    @JoinTable(name = "Cells")
    @JoinColumn(name = "jail", referencedColumnName = "id")
    var cellEntitiesSet: Set<CellEntity> = setOf()
        set(value) {
            seatCounts = null  // Now invalid.
            field = value
        }


    override val cells: List<Short>
        get() {
            return seatCounts ?: computeSeatCounts().also {
                seatCounts = it
            }
        }


    /** Maps [cellEntitiesSet] to [FullJailPojo.cells] **/
    private fun computeSeatCounts(): List<Short> {
        var maxCellNumber = cellEntitiesSet
            .maxByOrNull { it.key.cellNumber }
            ?.key?.cellNumber ?: 0

        val counts = MutableList<Short>(maxCellNumber.toInt()) {
            // Fill with negative values first.
            -1
        }

        // Now for each existing cellNumber fill the coresponding position
        // with a valid value:
        for(cell in cellEntitiesSet) {
            val index = cell.key.cellNumber.toInt() - 1
            counts[index] = cell.seatCount
        }

        return counts
    }
}