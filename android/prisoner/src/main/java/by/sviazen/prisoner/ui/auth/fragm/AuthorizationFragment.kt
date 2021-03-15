package by.sviazen.prisoner.ui.auth.fragm

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import by.sviazen.core.ui.common.SviazenFragment
import by.sviazen.core.util.android.NavigationUtil
import by.sviazen.prisoner.R
import by.sviazen.prisoner.databinding.AuthorizationFragmBinding
import by.sviazen.prisoner.ui.auth.model.AuthorizationState
import by.sviazen.prisoner.ui.auth.vm.AuthorizationViewModel
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
            handleSignUp()
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

            is AuthorizationState.WrongUsername -> {
                showErrorDialog(
                    R.string.wrong_credentials_title,
                    R.string.username_not_exist_msg
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.WrongPassword -> {
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

            is AuthorizationState.PasswordNotConfirmed -> {
                showErrorDialog(
                    R.string.wrong_credentials_title,
                    R.string.password_not_confirmed_msg
                ) { vm.notifyStateConsumed() }
            }

            is AuthorizationState.Success -> {
                NavigationUtil.safeNavigate(
                    findNavController(),
                    R.id.fragmAuth,
                    R.id.actAuth
                ) { null }
            }
        }
    }


    private fun handleSignUp() {
        val preconditionsPassed = vm.awaitPasswordConfirm(username, password)
        if(preconditionsPassed) {
            findNavController().navigate(R.id.actConfirmPassword)
        }
    }

    private fun onClickShowInfo(clickedView: View, infoStringRes: Int) {
        val listener = ShowInfoOnClickListener(infoStringRes)
        clickedView.setOnClickListener(listener)
    }
}