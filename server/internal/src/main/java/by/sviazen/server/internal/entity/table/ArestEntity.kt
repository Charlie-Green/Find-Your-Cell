package by.sviazen.server.internal.entity.table

import by.sviazen.domain.entity.LightArest
import javax.persistence.*


@Entity
@Table(name = "Arests")
class ArestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int,

    @Column(name = "prisoner")
    var prisonerId: Int,

    @Column(name = "start")
    var start: Long,

    @Column(name = "end")
    var end: Long ) {

    constructor(): this(0, 0, 0L, 0L)


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