package by.zenkevich_churun.findcell.server.internal

import javax.persistence.Persistence


class DatabaseConnection {

    fun printPrisoners(){
        val entityMan = Persistence
            .createEntityManagerFactory("FindCell")
            .createEntityManager()

        val query = entityMan.createQuery("select p from Prisoner p", Prisoner::class.java)
        val prisoners = query.resultList

        for(p in prisoners) {
            println("${p.id}. ${p.name}: ${p.info}")
        }
        println("Totally ${prisoners.size} prisoners.")
    }
}