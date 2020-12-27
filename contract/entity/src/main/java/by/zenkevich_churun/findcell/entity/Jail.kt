package by.zenkevich_churun.findcell.entity


/** A place where [Prisoner]s can be placed. **/
abstract class Jail {

    /** Database ID. **/
    abstract val id: Int

    /** Jail's name in the app's default language. **/
    abstract val name: String

    /** Number of [Cell]s within this [Jail]. **/
    abstract val cellCount: Short
}