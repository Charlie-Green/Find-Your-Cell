package by.zenkevich_churun.findcell.server.internal

import javax.persistence.*


@Entity
@Table(name = "Prisoners")
class Prisoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    lateinit var name: String

    lateinit var info: String
}