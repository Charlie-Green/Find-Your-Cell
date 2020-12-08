package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
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
            if(result != null) {
                notifyUpdateScheduleSuccess()
                vm.notifyUpdateScheduleResultConsumed()
            }
        })
        
        vm.cellUpdateLD.observe(this, Observer { update ->
            if(update is CellUpdate.DeleteFailed) {
                notifyDeleteCellFailed()
                vm.notifyCellUpdateConsumed()
            }
        })
    }


    private fun notifySavePrisonerSuccess()
        = notifySuccess(R.string.save_prisoner_success_msg)

    private fun notifyUpdateScheduleSuccess()
        = notifySuccess(R.string.update_schedule_success_msg)

    private fun notifyDeleteCellFailed()
        = notifyError(R.string.delete_cell_failed_msg)


    private fun notifySuccess(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    private fun notifyError(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 3000).apply {
            setTextColor(Color.RED)
            show()
        }
    }
}