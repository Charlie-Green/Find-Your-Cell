package by.sviazen.prisoner.repo.jail

import android.content.Context
import by.sviazen.core.api.jail.JailsApi
import by.sviazen.core.repo.jail.GetJailsResult
import by.sviazen.core.repo.jail.JailsRepository
import by.sviazen.domain.entity.Cell
import by.sviazen.prisoner.db.JailsDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JailsRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: JailsApi
): JailsRepository {

    override fun jailsList(internet: Boolean): GetJailsResult {
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


    override fun cell(
        jailId: Int,
        cellNumber: Short,
        internet: Boolean
    ): Cell? {

        val dao = JailsDatabase
            .get(appContext)
            .jailsDao

        val jailName = JailsRepositoryInternal
            .jailName(dao, api, jailId)
            ?: return null

        return JailsRepositoryInternal.cell(
            appContext,
            api,
            jailId,
            jailName,
            cellNumber,
            internet
        )
    }
}