package by.sviazen.server.internal.entity.table

import by.sviazen.domain.entity.Jail
import javax.persistence.*


@Entity
@Table(name = "Jails")
class JailEntity: Jail() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int = 0

    @Column(name = "name")
    override var name: String = ""

    @Column(name = "cells")
    override var cellCount: Short = 0
}