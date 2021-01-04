package by.zenkevich_churun.findcell.remote.retrofit.sched.map


/** Retrieves data required to map a Schedule POJO
  * into the base [Schedule] class entity.
  * The interface is implemented at the business-logic level
  * (since its implementation depends on the general logic of fetching and caching data)
  * and is used by the network requests level. **/
interface SchedulePropertiesAccessor {

    fun jailName(jailId: Int): String
    fun seatCount(jailId: Int, cellNumber: Short): Short
}