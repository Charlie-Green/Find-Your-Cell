package by.zenkevich_churun.findcell.server.internal.entity

import by.zenkevich_churun.findcell.protocol.prisoner.entity.Prisoner
import javax.persistence.*


@Entity
@Table(name = "Prisoners")
class PrisonerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "username")
    lateinit var username: String

    @Column(name = "pass")
    lateinit var passwordHash: ByteArray

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "info")
    lateinit var info: String


    /** Converts this from [Prisoner] instance from the 'protocol' project. **/
    fun toPrisoner(contacts: List<ContactEntity>): Prisoner {
        val contactInstances = contacts.map { c ->
            c.toContact()
        }

        return Prisoner(
            id,
            username,
            passwordHash,
            name,
            info,
            contactInstances
        )
    }
}