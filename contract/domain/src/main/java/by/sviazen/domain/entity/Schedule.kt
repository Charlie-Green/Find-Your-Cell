package by.sviazen.domain.entity

import by.sviazen.domain.entity.Cell
import java.util.*


/** A day-to-day schedule of where a [Prisoner] was kept during 1 their [Arest]. **/
abstract class Schedule {

    /** The value of [Arest.id] for the [Arest] this [Schedule] is for. **/
    abstract val arestId: Int

    /** The first day of schedule. **/
    abstract val start: Long

    /** The last day of schedule (the day the [Prisoner] was released). **/
    abstract val end: Long

    /** All [Cell]s the user specified that, during their arest, they have been to.
      * [SchedulePeriod.cellIndex] field points to an element within this list. **/
    abstract val cells: List<Cell>

    /** [SchedulePeriod]s within this [Schedule].
      * Ideally, they cover the time period of this [Schedule],
      * but they don't have to since, for some days, information
      * about [Prisoner] place of location may be missing. **/
    abstract val periods: List<SchedulePeriod>
}