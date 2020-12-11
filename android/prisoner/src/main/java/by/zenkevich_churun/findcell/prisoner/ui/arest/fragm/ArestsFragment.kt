package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.arest.vm.ArestsViewModel
import kotlinx.android.synthetic.main.arests_fragm.*
import javax.inject.Inject


class ArestsFragment: Fragment(R.layout.arests_fragm) {

    @Inject
    lateinit var vm: ArestsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initRecycler()
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ArestsViewModel.get(appContext, this)
    }

    private fun initRecycler() {
        recvArests.layoutManager = LinearLayoutManager(requireContext())
        recvArests.adapter = ArestsAdapter()
    }
}