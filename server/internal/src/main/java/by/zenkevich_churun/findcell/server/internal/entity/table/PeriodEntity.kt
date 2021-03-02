package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.domain.entity.SchedulePeriod
import by.zenkevich_churun.findcell.server.internal.entity.key.PeriodKey
import javax.persistence.*


@Entity
@Table(name = "Periods")
class PeriodEntity {

    @EmbeddedId
    lateinit var key: PeriodKey

    @Column(name = "jail")
    var jailId: Int = 0

    @Column(name = "cell")
    var cellNumber: Short = 0


    companion object {

        fun from(
            arestId: Int,
            period: SchedulePeriod,
            cells: List<ScheduleCellEntryEntity>
        ): PeriodEntity {

            val entity = PeriodEntity()
            entity.key = PeriodKey(
                arestId,
                period.start,
                period.end
            )

            val cellKey = cells[period.cellIndex].key!!
            entity.jailId = cellKey.jailId
            entity.cellNumber = cellKey.cellNumber

            return entity
        }
    }
}