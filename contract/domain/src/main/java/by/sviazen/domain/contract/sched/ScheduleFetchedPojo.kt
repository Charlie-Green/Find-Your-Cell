package by.sviazen.domain.contract.sched

import by.sviazen.domain.entity.*
import com.google.gson.annotations.SerializedName


/** server -> client to get a [Schedule]. **/
open class ScheduleFetchedPojo(

    @SerializedName("cells")
    var cells: List<CellPojo>,

    @SerializedName("periods")
    var periods: List<SchedulePeriodFetchedPojo> ) {


    constructor(): this(emptyList(), emptyList())
}