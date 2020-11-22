package by.zenkevich_churun.findcell.core.entity

import by.zenkevich_churun.findcell.core.entity.Contact


/** A person, who's been in prison and is searching for other such people.
  * In other words, the app's user. **/
abstract class Prisoner {

    /** Database ID. Assigned by the server.
      * Can be a real ID or a specific value to specify a newly created [Prisoner]
      * who hasn't been added to the database. **/
    abstract val id: Int

    /** Name created by the user and visible to everyone.
      * The user creates their name within the app so that their co-prisoners
      * can identify them; however, the name doesn't have to be unique.
      * Can be 1 to [MAX_NAME_LEN] characters long. **/
    abstract val name: String

    /** [Contact]s the user specified so that other users can contact them.
      * This information is publicly visible. **/
    abstract val contacts: List<Contact>

    /** Additional info the user specified about themself. It is publicly visible.
      * Can be 0 to [MAX_INFO_LEN] characters long. **/
    abstract val info: String


    companion object {
        const val INVALID_ID   = 0
        const val MAX_NAME_LEN = 48
        const val MAX_INFO_LEN = 1024
    }
}