package by.sviazen.server.protocol.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@PropertySources(
    PropertySource("classpath:public.properties"),
    PropertySource("classpath:confidential.properties")
)
@EntityScan(
    "${SviazenApplication.ROOT_PACKAGE}.internal.entity"
)
@EnableJpaRepositories(basePackages = [
    "${SviazenApplication.ROOT_PACKAGE}.internal.dao"
])
@ComponentScan(value = [
    "${SviazenApplication.ROOT_PACKAGE}.protocol.controller"
])
@Import(
    RepositoryBeans::class,
    ControllerBeans::class
)
open class SviazenApplication: SpringApplication() {

    companion object {
        const val ROOT_PACKAGE = "by.sviazen.server"
    }
}