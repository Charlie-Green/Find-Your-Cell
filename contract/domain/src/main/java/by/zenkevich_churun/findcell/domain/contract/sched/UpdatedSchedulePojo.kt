package by.zenkevich_churun.findcell.domain.contract.sched

import com.google.gson.annotations.SerializedName


class UpdatedSchedulePojo(

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    cells: List<CellPojo>,

    periods: List<SchedulePeriodPojo>

): GotSchedulePojo(cells, periods)