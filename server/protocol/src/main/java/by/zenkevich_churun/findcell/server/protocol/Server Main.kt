package by.zenkevich_churun.findcell.server.protocol

import org.springframework.boot.runApplication
import by.zenkevich_churun.findcell.server.internal.Library


fun main(args: Array<String>) {
	println("value = ${Library.value}")
	runApplication<ServerApplication>()
}