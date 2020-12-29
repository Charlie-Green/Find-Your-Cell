package by.zenkevich_churun.findcell.serial.jail.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Jail
import com.google.gson.annotations.SerializedName


class JailPojo1: Jail() {

    @SerializedName("id")
    override var id: Int = 0

    @SerializedName("name")
    override var name: String = ""

    @SerializedName("cells")
    override var cellCount: Short = 0


    companion object {

        fun from(j: Jail): JailPojo1 {
            if(j is JailPojo1) {
                return j
            }

            return JailPojo1().apply {
                id = j.id
                name = j.name
                cellCount = j.cellCount
            }
        }
    }
}