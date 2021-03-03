package by.zenkevich_churun.findcell.remote.retrofit.cp

import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.domain.contract.cp.CoPrisonerDataPojo
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.domain.contract.cp.GetCoPrisonerResponse
import by.zenkevich_churun.findcell.domain.simpleentity.SimpleContact
import by.zenkevich_churun.findcell.domain.util.Base64Coder
import by.zenkevich_churun.findcell.domain.util.Deserializer
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject


class RetrofitCoPrisonersApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val base64: Base64Coder
): CoPrisonersApi {

    override fun connect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        val passwordBase64 = base64.encode(passwordHash)

        val response = createService()
            .connect(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return relationFromResponse(response)
    }

    override fun disconnect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        val passwordBase64 = base64.encode(passwordHash)

        val response = createService()
            .disconnect(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return relationFromResponse(response)
    }

    override fun getCoPrisoner(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): GetCoPrisonerResponse {

        val passwordBase64 = base64.encode(passwordHash)

        val response = createService()
            .coPrisoner(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()

        if(response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            return GetCoPrisonerResponse.NotConnected
        }
        RetrofitApisUtil.assertResponseCode(response.code())

        val istream = response.body()!!.byteStream()
        val pojo = Deserializer.fromJsonStream(
            response.body()!!.byteStream(),
            CoPrisonerDataPojo::class.java
        )

        val contacts = mutableListOf<Contact>()
        addContact(contacts, pojo.phone,     Contact.Type.PHONE)
        addContact(contacts, pojo.telegram,  Contact.Type.TELEGRAM)
        addContact(contacts, pojo.viber,     Contact.Type.VIBER)
        addContact(contacts, pojo.whatsapp,  Contact.Type.WHATSAPP)
        addContact(contacts, pojo.vk,        Contact.Type.VK)
        addContact(contacts, pojo.skype,     Contact.Type.SKYPE)
        addContact(contacts, pojo.facebook,  Contact.Type.FACEBOOK)
        addContact(contacts, pojo.instagram, Contact.Type.INSTAGRAM)

        return GetCoPrisonerResponse.Success(contacts, pojo.info)
    }


    private fun createService(): CoPrisonersService {
        return retrofitHolder
            .retrofit
            .create(CoPrisonersService::class.java)
    }

    private fun relationFromResponse(
        response: Response<ResponseBody>
    ): CoPrisoner.Relation {
        val ordinal = response.body()!!.string().toInt()
        return CoPrisoner.Relation.values()[ordinal]
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