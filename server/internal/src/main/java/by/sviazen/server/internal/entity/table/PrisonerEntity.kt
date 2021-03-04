package by.sviazen.server.internal.entity.table

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