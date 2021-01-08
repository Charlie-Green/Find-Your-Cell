package by.zenkevich_churun.findcell.entity.pojo


/** A large POJO containing all synced data. **/
abstract class SynchronizedPojo {
    abstract val jails: List<FullJailPojo>
    abstract val coPrisoners: List<CoPrisonerPojo>
}