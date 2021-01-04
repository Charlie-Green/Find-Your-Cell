package by.zenkevich_churun.findcell.prisoner.repo.sched.map

import android.content.Context
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.prisoner.db.JailsDatabase
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepositoryInternal
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject


class SchedulePropertiesAccessorImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val jailsApi: JailsApi
): SchedulePropertiesAccessor {

    override fun jailName(jailId: Int): String {
        val jailsDao = JailsDatabase
            .get(appContext)
            .jailsDao

        return JailsRepositoryInternal
            .jailName(jailsDao, jailsApi, jailId)
            ?: throw IOException(
                "Jail with ID $jailId was not found, nor could it be fetched from the server"
            )
    }


    override fun seatCount(jailId: Int, cellNumber: Short): Short {

        val cell = JailsRepositoryInternal.cell(
            appContext,
            jailsApi,
            jailId,
            jailName(jailId),
            cellNumber,
            true  // Internet is checked before Schedule is fetched.
        )

        return cell?.seats ?: throw IOException(
            "Cannot retrieve a Cell. jailId=$jailId, cellNumber=$cellNumber" )
    }
}