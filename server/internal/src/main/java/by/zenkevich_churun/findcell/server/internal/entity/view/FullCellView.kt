package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.server.internal.entity.table.*
import javax.persistence.*


/** Join result of [CellEntity] and [JailEntity] **/
@Entity
@Table(name = "Cells")
class FullCellView(

    @ManyToOne(targetEntity = JailEntity::class)
    @JoinColumn(name = "jail", referencedColumnName = "id")
    var jail: JailEntity,

    @Id
    @Column(name = "number")
    var number: Short,

    @Column(name = "seats")
    var seats: Short ) {


    constructor(): this(JailEntity(), -1, -1)
}