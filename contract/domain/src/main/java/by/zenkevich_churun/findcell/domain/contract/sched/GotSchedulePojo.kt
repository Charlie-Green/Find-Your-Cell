package by.zenkevich_churun.findcell.domain.contract.sched

import by.zenkevich_churun.findcell.domain.entity.*
import com.google.gson.annotations.SerializedName


/** server -> client to get a [Schedule]. **/
open class GotSchedulePojo(

    @SerializedName("cells")
    var cells: List<CellPojo>,

    @SerializedName("periods")
    var periods: List<SchedulePeriodPojo> ) {


    constructor(): this(emptyList(), emptyList())
}