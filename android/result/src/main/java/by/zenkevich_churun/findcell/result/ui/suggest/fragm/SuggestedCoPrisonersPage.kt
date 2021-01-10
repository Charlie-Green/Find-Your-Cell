package by.zenkevich_churun.findcell.result.ui.suggest.fragm

import android.view.LayoutInflater
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.result.databinding.PageCpsSuggestedBinding


class SuggestedCoPrisonersPage: SviazenFragment<PageCpsSuggestedBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = PageCpsSuggestedBinding.inflate(inflater)
}