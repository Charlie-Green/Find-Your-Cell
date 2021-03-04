package by.sviazen.server.internal.entity.view

import by.sviazen.server.internal.entity.table.*


class ScheduleView(
    val arest: ArestEntity,
    val cellEntries: List<ScheduleCellEntryEntity>,
    val periodEntities: List<PeriodEntity>
)