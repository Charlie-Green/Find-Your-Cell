package by.sviazen.server.protocol.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.Persistence


@Configuration
open class DatabaseBeans {

    @Bean
    open fun entityManagerFactory()
        = Persistence.createEntityManagerFactory("FindCell")
}