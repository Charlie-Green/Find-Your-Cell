package by.sviazen.domain.contract.sched

import by.sviazen.domain.entity.*
import com.google.gson.annotations.SerializedName


/** Unlike [SchedulePeriodFetchedPojo], this class identifies a [Cell] entry
  * by [Cell] key ([Jail] ID + [Cell] number) rather than an index.
  * For updating a [Schedule] this approach is less error-prone
  * since [SchedulePeriod] are updated separately from [Cell] entries
  * (and thus, inconsistency might take place if [Cell]s are identified by index). **/
class SchedulePeriodUpdatedPojo(

    @SerializedName("start")
    var start: Long,

    @SerializedName("end")
    var end: Long,

    @SerializedName("jail")
    var jailId: Int,

    @SerializedName("cell")
    var cellNumber: Short ) {


    constructor(): this(0L, 0L, Jail.UNKNOWN_ID, -1)


    companion object {

        fun from(
            p: SchedulePeriod,
            cells: List<Cell>
        ): SchedulePeriodUpdatedPojo {

            val cell = cells[p.cellIndex]
            return SchedulePeriodUpdatedPojo(
                p.start,
                p.end,
                cell.jailId,
                cell.number
            )
        }
    }
}