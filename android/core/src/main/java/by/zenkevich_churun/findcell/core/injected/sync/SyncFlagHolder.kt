package by.zenkevich_churun.findcell.core.injected.sync


/** Holds a special flag - the sync flag - indicating whether the app
  * should start auto-sync when the user enters either of [CoPrisoner]s screens. **/
interface SyncFlagHolder {

    /** Return the sync flag's current value and set to false.
      * This method does not affect sync schedule nor caches. **/
    fun consume(): Boolean

    /** Set the flag. If set to false, any scheduled sync
      * is cancelled and [CoPrisoner]s cache is cleared. **/
    fun set(value: Boolean)
}