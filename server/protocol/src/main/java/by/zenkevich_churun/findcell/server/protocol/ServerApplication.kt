package by.zenkevich_churun.findcell.server.protocol

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*


@SpringBootApplication
open class ServerApplication: SpringApplication() {

    @Configuration
    @PropertySources(
        PropertySource("classpath:public.properties"),
        PropertySource("classpath:confidential.properties")
    )
    open class Config
}