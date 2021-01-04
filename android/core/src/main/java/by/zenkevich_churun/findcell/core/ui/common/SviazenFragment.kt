package by.zenkevich_churun.findcell.core.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


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


    protected abstract fun inflateViewBinding(
        inflater: LayoutInflater
    ): ViewBindingType
}