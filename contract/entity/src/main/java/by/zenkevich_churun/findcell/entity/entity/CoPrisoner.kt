package by.zenkevich_churun.findcell.entity.entity


/** Information about another (not the one who logged in) user
  * that the current user may see. **/
abstract class CoPrisoner {

    /** The value of [Prisoner.id] for the non-current user. **/
    abstract val id: Int

    /** The value of [Prisoner.name] for the non-current user. **/
    abstract val name: String

    /** The value of [Prisoner.contacts] for the non-current user,
      * or an empty [List] if [relation] is not [Relation.CONNECTED]. **/
    abstract val contacts: List<Contact>

    /** [Relation] between the two users. **/
    abstract val relation: CoPrisoner.Relation


    /** Possible reasons why the server would let the current user
      * see information about this user. **/
    enum class Relation {

        /** According to the data from both users,
          * the app supposes these two have met in a [Jail].
          * So the app suggests the users to connect. **/
        SUGGESTED,

        /** This user has sent connect request to the current one,
          * and the request is still pending (hasn't been confirmed nor declined,
          * or it was confirmed first but then cancelled). **/
        INCOMING_REQUEST,

        /** Opposite to [INCOMING_REQUEST]: this user
          * has pending request from the current one. **/
        OUTCOMING_REQUEST,

        /** The users have agreed to connect.
          * This is the only type of [Relation] which allows both users
          * to see each other's private information **/
        CONNECTED
    }
}