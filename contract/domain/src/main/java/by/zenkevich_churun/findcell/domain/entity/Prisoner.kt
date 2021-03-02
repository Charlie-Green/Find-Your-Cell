package by.zenkevich_churun.findcell.domain.entity

import by.zenkevich_churun.findcell.domain.entity.Contact


abstract class Prisoner {

    /** Database ID. Assigned by the server.
      * Can be a real ID or a specific value to specify a newly created [Prisoner]
      * who hasn't been added to the database. **/
    abstract val id: Int

    /** String used for authorization.
      * Not publicly visible. Not modifiable. **/
    abstract val username: String?

    /** Hash of the user's password. Required for authorization.
      * As of Sviazeń 1.0, this is a SHA-512 hash
      * of UTF-8 representation of the user's password. **/
    abstract val passwordHash: ByteArray?

    /** Name created by the user and visible to everyone.
      * The user creates their name within the app so that their co-prisoners
      * can identify them; however, the name doesn't have to be unique.
      * Can be 1 to [MAX_NAME_LEN] characters long. **/
    abstract val name: String

    /** Additional info the user specified about themself. It is publicly visible.
      * Can be 0 to [MAX_INFO_LEN] characters long. **/
    abstract val info: String

    /** [Contact]s the user specified so that other users can contact them.
      * This information is publicly visible. **/
    abstract val contacts: List<Contact>


    companion object {

        /** Used as the value of [id] field to specify the [Prisoner] is newly created
          * (and thus, doesn't have an ID yet). **/
        const val INVALID_ID   = 0

        /** The maximum allowed value of [name.length]. **/
        const val MAX_NAME_LEN = 48

        /** The maximum allowed value of [info.length]. **/
        const val MAX_INFO_LEN = 1024
    }
}