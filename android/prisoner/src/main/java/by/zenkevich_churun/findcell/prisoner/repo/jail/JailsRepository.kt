package by.zenkevich_churun.findcell.prisoner.repo.jail

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.prisoner.db.JailsDatabase
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity
import by.zenkevich_churun.findcell.prisoner.db.entity.JailEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JailsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: JailsApi ) {

    fun jailsList(internet: Boolean): GetJailsResult {
        val dao = JailsDatabase.get(appContext).jailsDao
        val cached = dao.jails()
        if(!cached.isEmpty()) {
            return GetJailsResult.Success(cached)
        }

        if(!internet) {
            return GetJailsResult.FirstTimeNeedInternet
        }

        val fetched = JailsRepositoryInternal
            .fetchJailsList(api, dao)
            ?: return GetJailsResult.FirstTimeError

        return GetJailsResult.Success(fetched)
    }


    fun cell(
        jailId: Int,
        cellNumber: Short,
        internet: Boolean

    ): Cell? = JailsRepositoryInternal.cell(
        appContext, api,
        jailId, cellNumber,
        internet
    )


    companion object {
        internal const val LOGTAG = "FindCell-Jails"
    }
}