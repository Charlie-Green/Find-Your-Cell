package by.sviazen.domain.contract.jail

import com.google.gson.annotations.SerializedName


class SeatCountsPojo(

    @SerializedName("seats")
    var seatCounts: List<Short> ) {


    constructor(): this(emptyList())
}