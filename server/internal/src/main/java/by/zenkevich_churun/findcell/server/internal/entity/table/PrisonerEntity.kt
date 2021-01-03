package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import javax.persistence.*


@Entity
@Table(name = "Prisoners")
class PrisonerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "username")
    var username: String? = null

    @Column(name = "pass")
    var passwordHash: ByteArray? = null

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "info")
    lateinit var info: String
}