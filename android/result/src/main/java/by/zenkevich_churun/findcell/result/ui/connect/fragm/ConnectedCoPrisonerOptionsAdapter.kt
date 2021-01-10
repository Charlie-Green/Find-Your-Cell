package by.zenkevich_churun.findcell.result.ui.connect.fragm

import android.util.Log
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter


internal class ConnectedCoPrisonerOptionsAdapter: CoPrisonerOptionsAdapter {
    override fun label1(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_view_contacts
    }

    override fun label2(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_disconnect
    }

    override fun onSelected1(relation: CoPrisoner.Relation, position: Int) {
        Log.v("CharlieDebug", "View Contacts (position=$position)")
    }

    override fun onSelected2(relation: CoPrisoner.Relation, position: Int) {
        Log.v("CharlieDebug", "Disconnect(position=$position)")
    }
}