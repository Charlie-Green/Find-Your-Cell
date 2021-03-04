package by.sviazen.domain.entity


/** A place where [Prisoner]s can be placed. **/
abstract class Jail {

    /** Database ID. **/
    abstract val id: Int

    /** Jail's name in the app's default language. **/
    abstract val name: String

    /** Number of [Cell]s within this [Jail]. **/
    abstract val cellCount: Short


    companion object {
        /** Value of [Jail.id] identifying that this [Jail]'s ID is not known. **/
        const val UNKNOWN_ID = 0
    }
}