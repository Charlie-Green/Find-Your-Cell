package by.zenkevich_churun.findcell.result.ui.cps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonerItemBinding


internal class CoPrisonersAdapter:
RecyclerView.Adapter<CoPrisonersAdapter.CoPrisonerViewHolder>() {

    private var cps: List<CoPrisoner>? = null


    class CoPrisonerViewHolder(
        private val vb: CoprisonerItemBinding
    ): RecyclerView.ViewHolder(vb.root) {

        fun bind(cp: CoPrisoner) {
            val iconRes = CoPrisonerRelationIcons.iconResourceFor(cp.relation)

            vb.txtvName.text = cp.name
            vb.imgvRelation.setImageResource(iconRes)
        }
    }


    override fun getItemCount(): Int
        = cps?.size ?: 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoPrisonerViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val vb = CoprisonerItemBinding.inflate(inflater, parent, false)
        return CoPrisonerViewHolder(vb)
    }

    override fun onBindViewHolder(holder: CoPrisonerViewHolder, position: Int) {
        val cp = cps?.get(position) ?: return
        holder.bind(cp)
    }


    fun submitData(data: List<CoPrisoner>) {
        cps = data
        notifyDataSetChanged()
    }
}