package by.zenkevich_churun.findcell.server.internal.entity.table

import javax.persistence.*


@Entity
@Table(name = "Arests")
class ArestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "start")
    var start: Long = 0L

    @Column(name = "end")
    var end: Long = 0L
}