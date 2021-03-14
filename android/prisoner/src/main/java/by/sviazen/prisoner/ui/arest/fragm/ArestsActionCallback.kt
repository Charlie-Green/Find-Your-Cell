package by.sviazen.prisoner.ui.arest.fragm

import android.content.Context
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import by.sviazen.prisoner.R
import by.sviazen.prisoner.ui.arest.vm.ArestsViewModel


internal class ArestsActionCallback(
    private val context: Context,
    private val vm: ArestsViewModel,
    private val adapter: ArestsAdapter
): ActionMode.Callback2() {

    override fun onCreateActionMode(
        mode: ActionMode,
        menu: Menu
    ): Boolean {
        mode.menuInflater.inflate(R.menu.arests_contextual, menu)
        return true
    }

    override fun onPrepareActionMode(
        mode: ActionMode,
        menu: Menu
    ): Boolean {
        // Do nothing.
        return false
    }

    override fun onActionItemClicked(
        mode: ActionMode,
        item: MenuItem
    ): Boolean {
        suggestDelete()
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        vm.setCheckable(false)
    }


    private fun suggestDelete() {
        val checks = adapter.checkedIds
        if(checks.isEmpty()) {
            return
        }

        val resources = context.resources
        val title = resources.getQuantityString(
            R.plurals.delete_arests_title, checks.size )
        val messageHtml1 = resources.getQuantityString(
            R.plurals.delete_arests_msg_1, checks.size, checks.size )
        val messageHtml2 = resources.getQuantityString(
            R.plurals.delete_arests_msg_2, checks.size, checks.size )
        val messageHtml = "$messageHtml1 $messageHtml2"

        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage( HtmlCompat.fromHtml(messageHtml, 0) )
            .setPositiveButton(R.string.delete_yes) { dialog, _ ->
                vm.delete()
                dialog.dismiss()
            }.setNegativeButton(R.string.delete_no) { dialog, _ ->
                vm.cancelDelete()
                dialog.dismiss()
            }.show()
    }
}