package by.zenkevich_churun.findcell.domain.contract.arest

import by.zenkevich_churun.findcell.domain.entity.Arest


sealed class CreateOrUpdateArestResponse {

    /** This means [Arest] was not created because the specified range of dates
      * intersects with that of another [Arest]. **/
    class ArestsIntersect(

        /** The value of [Arest.id] for the intersected [Arest]. **/
        val intersectedId: Int
    ): CreateOrUpdateArestResponse()

    class Success(
        /** ID of the [Arest] updated,
          * or assigned valid ID of a new created [Arest]. **/
        val arestId: Int
    ): CreateOrUpdateArestResponse()

    object NetworkError: CreateOrUpdateArestResponse()
}