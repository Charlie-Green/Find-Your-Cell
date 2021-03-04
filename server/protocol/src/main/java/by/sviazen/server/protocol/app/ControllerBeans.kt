package by.sviazen.server.protocol.app

import by.sviazen.domain.util.Base64Coder
import by.sviazen.server.protocol.common.Base64ServerCoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class ControllerBeans {

    @Bean
    open fun base64Coder(): Base64Coder
        = Base64ServerCoder()
}