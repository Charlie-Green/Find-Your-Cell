package by.zenkevich_churun.findcell.server.protocol

import by.zenkevich_churun.findcell.server.internal.DatabaseConnection
import org.springframework.boot.runApplication


fun main(args: Array<String>) {
	// runApplication<ServerApplication>()
	DatabaseConnection().printPrisoners()
}