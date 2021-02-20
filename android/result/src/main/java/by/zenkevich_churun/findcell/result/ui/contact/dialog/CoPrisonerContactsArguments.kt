package by.zenkevich_churun.findcell.result.ui.contact.dialog

import android.os.Bundle
import androidx.core.os.bundleOf


internal class CoPrisonerContactsArguments(private val bundle: Bundle) {

    val coprisonerId: Int
        get() = bundle.getInt(ARG_COPRISONER_ID)

    val coprisonerName: String
        get() = bundle.getString(ARG_COPRISONER_NAME)!!


    companion object {
        private const val ARG_COPRISONER_ID = "id"
        private const val ARG_COPRISONER_NAME = "nam"


        fun createBundle(
            coprisonerId: Int,
            coprisonerName: String

        ) = bundleOf(
            ARG_COPRISONER_ID to coprisonerId,
            ARG_COPRISONER_NAME to coprisonerName
        )
    }
}