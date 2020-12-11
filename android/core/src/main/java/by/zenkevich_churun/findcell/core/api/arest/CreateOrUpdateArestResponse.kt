package by.zenkevich_churun.findcell.core.api.arest


sealed class CreateOrUpdateArestResponse {

    /** This means [Arest] was not created because the specified range of dates
      * intersects with that of another [Arest]. **/
    class ArestsIntersect(

        /** The value of [Arest.id] for the intersected [Arest]. **/
        val intersectedId: Int
    )

    object NetworkError: CreateOrUpdateArestResponse()
    object Success: CreateOrUpdateArestResponse()
}