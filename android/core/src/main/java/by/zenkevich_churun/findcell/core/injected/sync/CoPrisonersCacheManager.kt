package by.zenkevich_churun.findcell.core.injected.sync


/** This class's functionality is small yet is used from different places
  * within the app, which is why it's separated into a standalone class. **/
interface CoPrisonersCacheManager {

    /** Clearup cached [CoPrisoner]s and cancel all scheduled syncs. **/
    fun invalidate()

    /** If there is no scheduled sync, schedule one
      * to be executed now (in an insignificant amount of time). **/
    fun scheduleFirstSync()
}