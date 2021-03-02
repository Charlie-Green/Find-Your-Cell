package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.domain.entity.Jail
import by.zenkevich_churun.findcell.server.internal.entity.table.CellEntity
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

    override val cellCount: Short
        get() = cellEntitiesSet.size.toShort()
}