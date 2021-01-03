package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.server.internal.entity.table.*


class ScheduleView(
    val arest: ArestEntity,
    val cellEntries: List<CellScheduleEntryEntity>,
    val periodEntities: List<PeriodEntity>
)