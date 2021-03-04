package by.sviazen.server.internal.entity.view

import by.sviazen.domain.entity.Jail
import by.sviazen.server.internal.entity.table.CellEntity
import javax.persistence.*


@Entity
@Table(name = "Jails")
class FullJailView(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int,

    @Column(name = "name")
    override var name: String,

    @OneToMany(targetEntity = CellEntity::class)
    @JoinColumn(name = "jail", referencedColumnName = "id")
    var cellEntitiesSet: Set<CellEntity>

): Jail() {

    constructor(): this(Jail.UNKNOWN_ID, "", hashSetOf())


    override val cellCount: Short
        get() = cellEntitiesSet.size.toShort()
}