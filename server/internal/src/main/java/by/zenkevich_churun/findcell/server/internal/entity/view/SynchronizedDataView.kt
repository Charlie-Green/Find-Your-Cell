package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.pojo.*


class SynchronizedDataView(
    override val coPrisoners: List<CoPrisonerView>,
    override val jails: List<FullJailPojo>
): SynchronizedPojo()