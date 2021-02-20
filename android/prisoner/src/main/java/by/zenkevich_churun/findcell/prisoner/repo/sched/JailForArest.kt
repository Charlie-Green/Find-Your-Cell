package by.zenkevich_churun.findcell.prisoner.repo.sched

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod


internal class JailForArest(
    override val id: Int,
    override val name: String
): Jail() {

    override val cellCount: Short
        get() = 0  // Doesn't matter for Arests list

    companion object {

        fun from(
            cells: List<Cell>,
            period: SchedulePeriod
        ): JailForArest {

            val cell = cells[period.cellIndex]
            return JailForArest(
                cell.jailId,
                cell.jailName
            )
        }
    }
}