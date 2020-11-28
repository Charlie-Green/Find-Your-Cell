package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
                notifySavePrisonerSuccess()
                vm.notifySaveResultConsumed()
            }
        })

        vm.updateScheduleResultLD.observe(this, Observer { result ->
            Log.v("CharlieDEbug", "Update result = ${result?.javaClass?.simpleName}")
            if(result != null) {
                notifyUpdateScheduleSuccess()
                vm.notifyUpdateScheduleResultConsumed()
            }
        })
    }


    private fun notifySavePrisonerSuccess() {
        notifySuccess(R.string.save_prisoner_success_msg)
    }

    private fun notifyUpdateScheduleSuccess() {
        notifySuccess(R.string.update_schedule_success_msg)
    }


    private fun notifySuccess(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }
}