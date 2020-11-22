package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.profile.vm.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragm.*


class ProfileFragment: Fragment(R.layout.profile_fragm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecycler()
        val vm = getViewModel()

        vm.prisonerLD.observe(viewLifecycleOwner, Observer { prisoner ->
            displayPrisoner(prisoner)
        })
    }


    private fun prepareRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getViewModel(): ProfileViewModel {
        val appContext = requireContext().applicationContext
        return ProfileViewModel.get(appContext, this)
    }

    private fun displayPrisoner(prisoner: Prisoner) {
        tietName.setText(prisoner.name)
        recyclerView.adapter = ProfileRecyclerAdapter(prisoner)
    }
}