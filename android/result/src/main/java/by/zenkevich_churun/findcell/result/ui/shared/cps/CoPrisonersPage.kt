package by.zenkevich_churun.findcell.result.ui.shared.cps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonersPageBinding


/** The two pages are similar to each other
  * so this is the common parent class for them. **/
abstract class CoPrisonersPage: SviazenFragment<CoprisonersPageBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersPageBinding.inflate(inflater)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vb.recv.adapter = CoPrisonersRecyclerAdapter()
    }


    protected fun observe(cpld: LiveData< List<CoPrisoner> >) {
        cpld.observe(viewLifecycleOwner) { cps ->
            Log.v("CharlieDebug", "${cps.size} coprisoners arrived")

            val adapter = vb.recv.adapter as? CoPrisonersRecyclerAdapter
                ?: throw IllegalStateException("Adapter not set")
            adapter.submitData(cps)
        }
    }
}