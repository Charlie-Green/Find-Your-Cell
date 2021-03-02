package by.zenkevich_churun.findcell.server.protocol.app

import by.zenkevich_churun.findcell.domain.util.Base64Coder
import by.zenkevich_churun.findcell.server.protocol.common.Base64ServerCoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class ControllerBeans {

    @Bean
    open fun base64Coder(): Base64Coder
        = Base64ServerCoder()
}