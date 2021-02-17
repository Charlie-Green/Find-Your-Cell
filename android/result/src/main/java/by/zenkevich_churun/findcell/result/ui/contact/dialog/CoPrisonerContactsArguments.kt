package by.zenkevich_churun.findcell.result.ui.contact.dialog

import android.os.Bundle
import androidx.core.os.bundleOf


internal class CoPrisonerContactsArguments(private val bundle: Bundle) {

    val coprisonerId: Int
        get() = bundle.getInt(ARG_COPRISONER_ID)


    companion object {
        private const val ARG_COPRISONER_ID = "id"

        fun createBundle(coprisonerId: Int) = bundleOf(
            ARG_COPRISONER_ID to coprisonerId
        )
    }
}