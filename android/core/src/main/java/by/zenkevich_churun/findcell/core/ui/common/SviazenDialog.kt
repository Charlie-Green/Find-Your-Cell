package by.zenkevich_churun.findcell.core.ui.common

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding


/** Same purpose as [SviazenFragment], but derived from [DialogFragment]. **/
abstract class SviazenDialog<ViewBindingType: ViewBinding>: DialogFragment() {

    protected lateinit var vb: ViewBindingType


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vb = inflateViewBinding(inflater)
        customizeDialog(vb.root)
        return vb.root
    }


    protected abstract fun inflateViewBinding(
        inflater: LayoutInflater
    ): ViewBindingType

    /** A place to change dialog's dimensions and native background properties. **/
    protected open fun customizeDialog(view: View) {
        // Default implementation is empty.
    }
}