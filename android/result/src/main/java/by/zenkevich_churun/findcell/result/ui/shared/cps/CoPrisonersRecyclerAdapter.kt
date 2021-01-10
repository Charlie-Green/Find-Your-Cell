package by.zenkevich_churun.findcell.result.ui.shared.cps

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonerItemBinding


class CoPrisonersRecyclerAdapter(
    private val vm: CoPrisonersPageViewModel
): RecyclerView.Adapter<CoPrisonersRecyclerAdapter.CoPrisonerViewHolder>() {

    private var cps: List<CoPrisoner>? = null
    private var positionExpanded = -1


    class CoPrisonerViewHolder(
        private val vm: CoPrisonersPageViewModel,
        private val vb: CoprisonerItemBinding
    ): RecyclerView.ViewHolder(vb.root) {

        init {
            vb.root.setOnClickListener {
                vm.swapPositionExpandedStatus(adapterPosition)
            }
        }


        fun bind(cp: CoPrisoner, expanded: Boolean) {
            val iconRes = CoPrisonerRelationIcons.iconResourceFor(cp.relation)

            vb.txtvName.text = cp.name
            vb.imgvRelation.setImageResource(iconRes)

            setExpanded(expanded, false)
        }

        fun setExpanded(expanded: Boolean, animate: Boolean) {
            Log.v("CharlieDebug", "[$adapterPosition]: expanded=$expanded, animate=$animate")
            // TODO
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
        return CoPrisonerViewHolder(vm, vb)
    }

    override fun onBindViewHolder(holder: CoPrisonerViewHolder, position: Int) {
        val cp = cps?.get(position) ?: return
        holder.bind(cp, position == positionExpanded)
    }

    override fun onBindViewHolder(
        holder: CoPrisonerViewHolder,
        position: Int,
        payloads: MutableList<Any> ) {

        if(payloads.size == 1 && payloads[0] === PAYLOAD_SET_EXPANDED) {
            holder.setExpanded(position == positionExpanded, true)
        }
    }


    var expandedPosition: Int
        get() = positionExpanded
        set(value) {
            if(value != positionExpanded) {
                val oldPosition = positionExpanded
                positionExpanded = value
                notifyItemChanged(oldPosition)    // Collapse the oldItem
                notifyItemChanged(value)          // Expand the new item
            }
        }

    fun submitData(data: List<CoPrisoner>) {
        cps = data
        notifyDataSetChanged()
    }


    companion object {
        private val PAYLOAD_SET_EXPANDED = Any()
    }
}