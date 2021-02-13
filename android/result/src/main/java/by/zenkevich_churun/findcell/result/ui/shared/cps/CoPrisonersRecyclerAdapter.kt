package by.zenkevich_churun.findcell.result.ui.shared.cps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonerItemBinding


internal class CoPrisonersRecyclerAdapter(
    private val vm: CoPrisonersPageViewModel,
    private val optionsAdapter: CoPrisonerOptionsAdapter
): RecyclerView.Adapter<CoPrisonersRecyclerAdapter.CoPrisonerViewHolder>() {

    private var cps: List<CoPrisoner>? = null
    private var positionExpanded = -1


    class CoPrisonerViewHolder(
        private val vm: CoPrisonersPageViewModel,
        private val vb: CoprisonerItemBinding,
        private val optionsAdapter: CoPrisonerOptionsAdapter
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
            setupButtons(cp.relation)

            val context = vb.txtvInfo.context
            vb.txtvInfo.text = context.getString(
                R.string.cp_common_cell,
                cp.commonJailName,
                cp.commonCellNumber
            )

            setExpanded(expanded, false)
        }

        fun setExpanded(expanded: Boolean, animate: Boolean) {
            val heightRes =
                if(expanded) R.dimen.coprisoner_item_height_expanded
                else R.dimen.coprisoner_item_height_collapsed
            val resources = vb.root.context.resources
            setHeight( resources.getDimensionPixelSize(heightRes) )
        }


        private fun setupButtons(relation: CoPrisoner.Relation) {
            val label1 = optionsAdapter.label1(relation)
            val label2 = optionsAdapter.label2(relation)

            vb.bu1.setText(label1)
            vb.bu1.setOnClickListener {
                optionsAdapter.onSelected1(relation, adapterPosition)
            }

            if(label2 == 0) {
                vb.bu2.visibility = View.INVISIBLE
                vb.bu2.setOnClickListener(null)
            } else {
                vb.bu2.visibility = View.VISIBLE
                vb.bu2.setOnClickListener {
                    optionsAdapter.onSelected2(relation, adapterPosition)
                }
            }
        }


        private fun setHeight(h: Int) {
            vb.vlltExpandable.updateLayoutParams {
                height = h
            }
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
        return CoPrisonerViewHolder(vm, vb, optionsAdapter)
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
        } else {
            onBindViewHolder(holder, position)
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

    fun submitData(data: List<CoPrisoner>, updateEntirely: Boolean) {
        cps = data
        if(updateEntirely) {
            notifyDataSetChanged()
        }
    }


    companion object {
        private val PAYLOAD_SET_EXPANDED = Any()
    }
}