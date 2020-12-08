package by.zenkevich_churun.findcell.prisoner.ui.auth.fragm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.auth.vm.AuthorizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.authorization_fragm.*
import javax.inject.Inject


@AndroidEntryPoint
class AuthorizationFragment: Fragment(R.layout.authorization_fragm) {

    @Inject
    lateinit var vm: AuthorizationViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initLogo()
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = AuthorizationViewModel.get(appContext, this)
    }

    private fun initLogo() {
        val iconRes = resources.getIdentifier(
            "ic_app", "mipmap", requireContext().packageName )

        if(iconRes == 0) {
            imgvLogo.visibility = View.GONE
        } else {
            imgvLogo.setImageResource(iconRes)
        }
    }
}