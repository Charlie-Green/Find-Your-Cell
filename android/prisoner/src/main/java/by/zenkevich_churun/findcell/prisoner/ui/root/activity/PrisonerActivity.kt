package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.ui.root.vm.PrisonerRootViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.prisoner_activity.*


/** This [Activity] combines screens that set up
  * [Prisoner]'s schedule and profile. **/
@AndroidEntryPoint
class PrisonerActivity: AppCompatActivity(R.layout.prisoner_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = PrisonerRootViewModel.get(applicationContext, this)
        vm.savePrisonerResultLD.observe(this, Observer { result ->
            if(result == SavePrisonerResult.SUCCESS) {
                notifySaveSuccess()
                vm.notifySaveResultConsumed()
            }
        })
    }


    private fun notifySaveSuccess() {
        Snackbar.make(cdltRoot, R.string.save_prisoner_success_msg, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }
}