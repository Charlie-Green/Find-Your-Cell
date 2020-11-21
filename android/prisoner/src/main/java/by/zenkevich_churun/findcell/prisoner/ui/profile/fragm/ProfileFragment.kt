package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.prisoner.R
import kotlinx.android.synthetic.main.profile_fragm.*


class ProfileFragment: Fragment(R.layout.profile_fragm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // conv.show( Contact.Telegram("@my_telegram") )
        addContactView.setContent( listOf(
            Contact.Type.FACEBOOK,
            Contact.Type.TELEGRAM,
            Contact.Type.VIBER,
            Contact.Type.INSTAGRAM,
            Contact.Type.VK,
            Contact.Type.PHONE,
            Contact.Type.WHATSAPP
        ) )

        addContactView.setContactTypeSelectedListener { type ->
            // ...
        }
    }
}