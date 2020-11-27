package by.zenkevich_churun.findcell.prisoner.repo.sched

import by.zenkevich_churun.findcell.core.api.ScheduleApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleRepository @Inject constructor(
    private val api: ScheduleApi ) {

//    fun getSchedule(): GetScheduleResult {
//        return try {
//            api.get()
//        }
//    }
}