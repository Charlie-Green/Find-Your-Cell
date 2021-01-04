package by.zenkevich_churun.findcell.remote.retrofit.sched.entity

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.serial.sched.pojo.CellPojo


internal class DeserializedCell
private constructor(): Cell() {

    override var jailId: Int = 0
    override lateinit var jailName: String
    override var number: Short = 0
    override var seats: Short = 0


    companion object {

        fun from(
            pojo: CellPojo,
            props: SchedulePropertiesAccessor
        ): DeserializedCell {

            val cell = DeserializedCell()

            cell.jailId   = pojo.jailId
            cell.jailName = props.jailName(pojo.jailId)
            cell.number   = pojo.cellNumber
            cell.seats    = props.seatCount(pojo.jailId, pojo.cellNumber)

            return cell
        }
    }
}