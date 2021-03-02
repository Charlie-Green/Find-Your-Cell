package by.zenkevich_churun.findcell.domain.entity


/** A room within a [Jail] where [Prisoner]s live. **/
abstract class Cell {

    /** The value of [Jail.id] property
      * of the [Jail] this [Cell] belongs to. **/
    abstract val jailId: Int

    /** The value of [Jail.name] property
      * of the [Jail] this [Cell] belongs to. **/
    abstract val jailName: String

    /** The [Cell]'s number within its [Jail]. **/
    abstract val number: Short

    /** The maximum number of people this [Cell] can place at a moment. **/
    abstract val seats: Short
}