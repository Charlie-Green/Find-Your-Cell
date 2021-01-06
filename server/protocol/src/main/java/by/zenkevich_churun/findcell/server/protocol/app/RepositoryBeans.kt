package by.zenkevich_churun.findcell.server.protocol.app

import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import by.zenkevich_churun.findcell.server.internal.repo.cellentry.CellEntriesRepository
import by.zenkevich_churun.findcell.server.internal.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.server.internal.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.server.internal.repo.sched.ScheduleRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class RepositoryBeans {

    @Bean
    open fun authorizationRepository()
        = AuthorizationRepository()

    @Bean
    open fun profileRepository()
        = ProfileRepository()

    @Bean
    open fun arestsRepository()
        = ArestsRepository()

    @Bean
    open fun jailsRepository()
        = JailsRepository()

    @Bean
    open fun scheduleRepository()
        = ScheduleRepository()

    @Bean
    open fun cellEntriesRepository()
        = CellEntriesRepository()
}