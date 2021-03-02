package by.zenkevich_churun.findcell.prisoner.ui.auth.fragm

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.AuthorizationFragmBinding
import by.zenkevich_churun.findcell.prisoner.ui.auth.model.AuthorizationState
import by.zenkevich_churun.findcell.prisoner.ui.auth.vm.AuthorizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AuthorizationFragment: SviazenFragment<AuthorizationFragmBinding>() {

    @Inject
    lateinit var vm: AuthorizationViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = AuthorizationFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initLogo()

        vm.stateLD.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        vb.buLogIn.setOnClickListener {
            vm.logIn(username, password)
        }
        vb.buSignUp.setOnClickListener {
            vm.signUp(username, password)
        }
        onClickShowInfo(vb.imgvUsernameInfo, R.string.username_info)
        onClickShowInfo(vb.imgvPasswordInfo, R.string.password_info)
    }


    private val username: String
        get() = vb.tietUsername.text?.toString() ?: ""

    private val password: String
        get() = vb.tietPassword.text?.toString() ?: ""


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = AuthorizationViewModel.get(appContext, this)
    }

    private fun initLogo() {
        val iconRes = resources.getIdentifier(
            "ic_app", "mipmap", requireContext().packageName )

        if(iconRes == 0) {
            vb.imgvLogo.visibility = View.GONE
        } else {
            vb.imgvLogo.setImageResource(iconRes)
        }
    }


    private fun renderState(state: AuthorizationState) {
        if(state is AuthorizationState.Loading) {
            vb.prBar.visibility = View.VISIBLE
            return
        }
        vb.prBar.visibility = View.GONE

        when(state) {
            is AuthorizationState.NetworkError -> {
                val messageRes =
                    if(state.wasLoggingIn) R.string.login_failed_msg
                    else R.string.signup_failed_msg
                showErrorDialog(R.string.error_title, messageRes) {
                    vm.notifyStateConsumed()
                }
            }

            is AuthorizationState.InvalidUsername -> {
                showErrorDialog(
                    R.string.invalid_username_title,
                    R.string.username_rules
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.InvalidPassword -> {
                showErrorDialog(
                    R.string.invalid_password_title,
                    R.string.password_rules
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.NoInternet -> {
                showErrorDialog(
                    R.string.no_internet_title,
                    R.string.auth_needs_internet_msg
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.UsernameNotExist -> {
                showErrorDialog(
                    R.string.wrong_credentials_title,
                    R.string.username_not_exist_msg
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.PasswordNotMatch -> {
                showErrorDialog(
                    R.string.wrong_credentials_title,
                    R.string.password_not_match_msg
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.UsernameTaken -> {
                showErrorDialog(
                    getString(R.string.invalid_username_title),
                    getString(R.string.username_taken_msg, state.username)
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.Success -> {
                NavigationUtil.safeNavigate(
                    findNavController(),
                    R.id.fragmAuth,
                    R.id.actLogIn
                ) { null }
            }
        }
    }


    
    private fun onClickShowInfo(clickedView: View, infoStringRes: Int) {
        val listener = ShowInfoOnClickListener(infoStringRes)
        clickedView.setOnClickListener(listener)
    }
}