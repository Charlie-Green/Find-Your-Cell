package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.os.Bundle
import android.view.*
import by.zenkevich_churun.findcell.prisoner.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddArestBottomSheet: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.add_arest_bottomsheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}