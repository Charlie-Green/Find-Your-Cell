package by.zenkevich_churun.findcell.prisoner.ui.auth.fragm

import android.view.View
import androidx.appcompat.app.AlertDialog
import by.zenkevich_churun.findcell.core.util.android.DialogUtil
import by.zenkevich_churun.findcell.prisoner.R


internal class ShowInfoOnClickListener(
    private val messageRes: Int
): View.OnClickListener {

    override fun onClick(v: View) {
        val dialog = AlertDialog
            .Builder(v.context)
            .setMessage(messageRes)
            .create()

        val res = v.context.resources
        val dialogWidth = res.getDimensionPixelSize(R.dimen.auth_info_width)
        val parent = v.parent as? View
        val x = (v.x + (parent?.x ?: 0f)).toInt()
        val y = (v.y + (parent?.y ?: 0f)).toInt()

        DialogUtil.setWidth(dialog, dialogWidth)
        DialogUtil.showAt(dialog, x - dialogWidth, y)
    }
}