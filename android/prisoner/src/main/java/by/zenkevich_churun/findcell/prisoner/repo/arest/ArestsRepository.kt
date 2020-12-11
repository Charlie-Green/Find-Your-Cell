package by.zenkevich_churun.findcell.prisoner.repo.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArestsRepository @Inject constructor(
    private val arestsApi: ArestsApi,
    private val jailsApi: JailsApi
) {


}