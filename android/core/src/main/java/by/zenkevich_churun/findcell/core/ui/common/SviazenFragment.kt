package by.zenkevich_churun.findcell.core.ui.common

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import by.zenkevich_churun.findcell.core.R


/** Common super class for all [Fragment]s within the app.
  * Implements some common functionality, majorly the view binding. **/
abstract class SviazenFragment<ViewBindingType: ViewBinding>: Fragment() {

    protected lateinit var vb: ViewBindingType


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vb = inflateViewBinding(inflater)
        return vb.root
    }
    
    
    protected fun showErrorDialog(
        title: String,
        message: String,
        onDismiss: (DialogInterface) -> Unit ) {

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener(onDismiss)
            .show()
    }

    protected fun showErrorDialog(
        titleRes: Int,
        messageRes: Int,
        onDismiss: (DialogInterface) -> Unit

    ) = showErrorDialog(
        getString(titleRes),
        getString(messageRes),
        onDismiss
    )


    protected abstract fun inflateViewBinding(
        inflater: LayoutInflater
    ): ViewBindingType
}