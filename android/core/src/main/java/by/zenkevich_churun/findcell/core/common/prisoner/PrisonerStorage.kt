package by.zenkevich_churun.findcell.core.common.prisoner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.domain.contract.auth.AuthorizedPrisonerPojo
import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.domain.entity.Prisoner
import by.zenkevich_churun.findcell.domain.simpleentity.SimpleContact
import by.zenkevich_churun.findcell.domain.simpleentity.SimplePrisoner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerStorage @Inject constructor() {
    private val mldPrisoner = MutableLiveData<Prisoner?>()

    val prisonerLD: LiveData<Prisoner?>
        get() = mldPrisoner

    fun submit(prisoner: AuthorizedPrisonerPojo, passwordHash: ByteArray) {
        val contacts = mutableListOf<Contact>()
        addContact(contacts, prisoner.phone,     Contact.Type.PHONE)
        addContact(contacts, prisoner.telegram,  Contact.Type.TELEGRAM)
        addContact(contacts, prisoner.viber,     Contact.Type.VIBER)
        addContact(contacts, prisoner.whatsapp,  Contact.Type.WHATSAPP)
        addContact(contacts, prisoner.vk,        Contact.Type.VK)
        addContact(contacts, prisoner.skype,     Contact.Type.SKYPE)
        addContact(contacts, prisoner.facebook,  Contact.Type.FACEBOOK)
        addContact(contacts, prisoner.instagram, Contact.Type.INSTAGRAM)

        val ep = SimplePrisoner(
            prisoner.id,
            null,
            passwordHash,
            prisoner.name,
            prisoner.info,
            contacts
        )

        mldPrisoner.postValue(ep)
    }

    fun submit(prisoner: Prisoner) {
        if(prisoner.username != null) {
            throw IllegalArgumentException("Username must not be stored")
        }
        if(prisoner.passwordHash == null) {
            throw IllegalArgumentException("Password hash must be stored")
        }

        mldPrisoner.postValue(prisoner)
    }

    fun clear() {
        mldPrisoner.postValue(null)
    }


    private fun addContact(
        target: MutableCollection<Contact>,
        data: String?,
        type: Contact.Type ) {

        data ?: return
        val contact = SimpleContact(type, data)
        target.add(contact)
    }
}