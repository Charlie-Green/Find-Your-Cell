package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.entity.Jail
import javax.persistence.*


@Entity
@Table(name = "Jails")
class JailEntity: Jail() {

    @Column(name = "id")
    override var id: Int = 0

    @Column(name = "name")
    override var name: String = ""

    @Column(name = "cells")
    override var cellCount: Short = 0
}