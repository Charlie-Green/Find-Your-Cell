package by.zenkevich_churun.findcell.prisoner.ui.interrupt.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R


class EditInterruptDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.result_request_dialog, container, false).also { view ->
            setDialogWidth(view)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


    private fun setDialogWidth(view: View) {
        val screenSize = AndroidUtil.activitySize(requireActivity())
        val desiredWidth = (screenSize.width*4)/5
        view.minimumWidth = desiredWidth
    }
}