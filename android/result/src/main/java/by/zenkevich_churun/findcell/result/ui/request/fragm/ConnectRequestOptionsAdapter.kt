package by.zenkevich_churun.findcell.result.ui.request.fragm

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.request.vm.ConnectRequestsViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter


class ConnectRequestOptionsAdapter(
    private val vm: ConnectRequestsViewModel
): CoPrisonerOptionsAdapter {

    override fun label1(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_confirm
    }

    override fun label2(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_decline
    }

    override fun onSelected1(relation: CoPrisoner.Relation, position: Int) {
        vm.confirmRequest(position)
    }

    override fun onSelected2(relation: CoPrisoner.Relation, position: Int) {
        vm.declineRequest(position)
    }
}