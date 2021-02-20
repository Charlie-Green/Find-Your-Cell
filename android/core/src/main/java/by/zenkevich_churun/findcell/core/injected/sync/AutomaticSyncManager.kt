package by.zenkevich_churun.findcell.core.injected.sync


/** This interface is to be used by repositories
  * to manage automatic sync and synchronized data **/
interface AutomaticSyncManager {

    /** Returns the flag's current value without affecting it. **/
    fun get(): Boolean

    /** Return the sync flag's current value and set to false.
      * This method does not affect sync schedule nor caches. **/
    fun consume(): Boolean

    /** Set the flag.
      * - If set to false, any scheduled sync is cancelled. **/
    fun set(value: Boolean)

    /** Clears cached [CoPrisoner]s data. **/
    fun clearCoPrisonersCache()
}