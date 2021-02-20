package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.entity.LightArest
import javax.persistence.*


@Entity
@Table(name = "Arests")
class ArestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "prisoner")
    var prisonerId: Int = 0

    @Column(name = "start")
    var start: Long = 0L

    @Column(name = "end")
    var end: Long = 0L


    companion object {

        fun from(
            arest: LightArest,
            prisonerId: Int
        ): ArestEntity {

            val entity = ArestEntity()
            entity.id = arest.id
            entity.prisonerId = prisonerId
            entity.start = arest.start
            entity.end = arest.end

            return entity
        }
    }
}