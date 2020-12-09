package by.zenkevich_churun.findcell.prisoner.ui.auth.fragm

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.zenkevich_churun.findcell.core.util.android.DialogUtil
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.auth.model.AuthorizationState
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

        vm.stateLD.observe(viewLifecycleOwner, Observer { state ->
            renderState(state)
        })

        buLogIn.setOnClickListener {
            vm.logIn(username, password)
        }
        buSignUp.setOnClickListener {
            vm.signUp(username, password)
        }
        onClickShowInfo(imgvUsernameInfo, R.string.username_info)
        onClickShowInfo(imgvPasswordInfo, R.string.password_info)
    }


    private val username: String
        get() = tietUsername.text?.toString() ?: ""

    private val password: String
        get() = tietPassword.text?.toString() ?: ""


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


    private fun renderState(state: AuthorizationState) {
        if(state is AuthorizationState.Loading) {
            prBar.visibility = View.VISIBLE
            return
        }
        prBar.visibility = View.GONE

        when(state) {
            is AuthorizationState.NetworkError -> {
                val messageRes =
                    if(state.wasLoggingIn) R.string.login_failed_msg
                    else R.string.signup_failed_msg
                notifyError(R.string.error_title, messageRes)
            }

            is AuthorizationState.InvalidUsername -> {
                notifyError(R.string.invalid_username_title, R.string.username_rules)
            }

            is AuthorizationState.InvalidPassword -> {
                notifyError(R.string.invalid_password_title, R.string.password_rules)
            }

            is AuthorizationState.NoInternet -> {
                notifyError(R.string.no_internet_title, R.string.auth_needs_internet_msg)
            }

            is AuthorizationState.UsernameNotExist -> {
                notifyError(R.string.wrong_credentials_title, R.string.username_not_exist_msg)
            }

            is AuthorizationState.PasswordNotMatch -> {
                notifyError(R.string.wrong_credentials_title, R.string.password_not_match_msg)
            }

            is AuthorizationState.UsernameTaken -> {
                val msg = getString(R.string.username_taken_msg, state.username)
                notifyError(R.string.invalid_username_title, msg)
            }

            is AuthorizationState.Success -> {
                NavigationUtil.safeNavigate(
                    findNavController(),
                    R.id.fragmAuth,
                    R.id.actLogIn
                )
            }
        }
    }


    private fun notifyError(titleRes: Int, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(titleRes)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener {
                vm.notifyStateConsumed()
            }.create()

        DialogUtil.showAt(dialog, 200, 400)
    }

    private fun notifyError(titleRes: Int, messageRes: Int)
        = notifyError(titleRes, getString(messageRes))


    private fun onClickShowInfo(clickedView: View, infoStringRes: Int) {
        val listener = ShowInfoOnClickListener(infoStringRes)
        clickedView.setOnClickListener(listener)
    }
}