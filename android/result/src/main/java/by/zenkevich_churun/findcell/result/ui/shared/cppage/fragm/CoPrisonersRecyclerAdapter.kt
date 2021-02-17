package by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonerItemBinding
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel


internal class CoPrisonersRecyclerAdapter<ViewModelType: CoPrisonersPageViewModel>(
    private val vm: ViewModelType,
    private val pageDescriptor: CoPrisonersPageDescriptor<ViewModelType>
): RecyclerView.Adapter< CoPrisonersRecyclerAdapter.CoPrisonerViewHolder<ViewModelType> >() {

    private var cps: List<CoPrisoner>? = null
    private var positionExpanded = -1


    class CoPrisonerViewHolder<ViewModelType: CoPrisonersPageViewModel>(
        private val vm: ViewModelType,
        private val vb: CoprisonerItemBinding,
        private val pageDescriptor: CoPrisonersPageDescriptor<ViewModelType>
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

            setExpanded(cp, expanded)
        }

        fun setExpanded(cp: CoPrisoner, expanded: Boolean) {
            val heightRes =
                if(expanded) R.dimen.coprisoner_item_height_expanded
                else R.dimen.coprisoner_item_height_collapsed
            val resources = vb.root.context.resources
            setHeight( resources.getDimensionPixelSize(heightRes) )

            setAlpha(cp.relation)
        }


        private fun setupButtons(relation: CoPrisoner.Relation) {
            val label1 = pageDescriptor.label1(relation)
            val label2 = pageDescriptor.label2(relation)

            vb.bu1.setText(label1)
            vb.bu1.setOnClickListener {
                pageDescriptor.onSelected1(vm, relation, adapterPosition)
            }

            if(label2 == 0) {
                vb.bu2.visibility = View.INVISIBLE
                vb.bu2.setOnClickListener(null)
            } else {
                vb.bu2.visibility = View.VISIBLE
                vb.bu2.setText(label2)
                vb.bu2.setOnClickListener {
                    pageDescriptor.onSelected2(vm, relation, adapterPosition)
                }
            }
        }

        private fun setHeight(h: Int) {
            vb.vlltExpandable.updateLayoutParams {
                height = h
            }
        }


        private fun setAlpha(relation: CoPrisoner.Relation) {
            val isDeclinedRequest = (relation == CoPrisoner.Relation.REQUEST_DECLINED)
            vb.vlltExpandable.alpha = if(isDeclinedRequest) 0.6f else 1.0f
        }
    }


    override fun getItemCount(): Int
        = cps?.size ?: 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoPrisonerViewHolder<ViewModelType> {

        val inflater = LayoutInflater.from(parent.context)
        val vb = CoprisonerItemBinding.inflate(inflater, parent, false)
        return CoPrisonerViewHolder(vm, vb, pageDescriptor)
    }

    override fun onBindViewHolder(
        holder: CoPrisonerViewHolder<ViewModelType>,
        position: Int ) {

        val cp = cps?.get(position) ?: return
        holder.bind(cp, position == positionExpanded)
    }

    override fun onBindViewHolder(
        holder: CoPrisonerViewHolder<ViewModelType>,
        position: Int,
        payloads: MutableList<Any> ) {

        if(payloads.size == 1 && payloads[0] === PAYLOAD_SET_EXPANDED) {
            val cp = cps?.get(position) ?: return
            holder.setExpanded(cp, position == positionExpanded)
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

    fun submitData(data: List<CoPrisoner>, updatedPosition: Int) {
        cps = data

        if(updatedPosition < 0) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(updatedPosition)
        }
    }


    companion object {
        private val PAYLOAD_SET_EXPANDED = Any()
    }
}