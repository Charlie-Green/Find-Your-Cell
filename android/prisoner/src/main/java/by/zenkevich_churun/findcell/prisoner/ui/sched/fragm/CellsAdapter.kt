package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel
import by.zenkevich_churun.findcell.prisoner.util.view.cell.CellView


internal class CellsAdapter(
    private val cells: List<CellModel>,
    private val vm: ScheduleViewModel
): RecyclerView.Adapter<CellsAdapter.CellViewHolder>() {

    private var positionSelected = -1


    inner class CellViewHolder(
        private val cellView: CellView
    ): RecyclerView.ViewHolder(cellView) {

        init {
            cellView.setOnClickListener {
                vm.selectCell(adapterPosition)
            }
        }

        fun bind(cell: CellModel) {
            cellView.apply {
                setBackgroundColor(cell.backColor)
                setNumberBackgroundColor(cell.numberBackColor)
                setTextColor(cell.textColor)
                show(cell)
            }
        }

        fun setSelection(selected: Boolean) {
            val scale = if(selected) 1f else 0.85f
            cellView.apply {
                pivotX = 0.5f*width
                pivotY = 0.5f*height
                scaleX = scale
                scaleY = scale
                alpha = if(selected) 1f else 0.7f
            }
        }
    }


    override fun getItemCount(): Int
        = cells.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val res = parent.context.resources
        val itemHeight = res.getDimensionPixelSize(R.dimen.cellview_height)
        val itemMargin = res.getDimensionPixelSize(R.dimen.cellview_margin)

        val view = CellView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, itemHeight).apply {
            setMargins(itemMargin)
        }

        return CellViewHolder(view)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(cells[position])
        setViewHolderSelection(holder, position)
    }

    override fun onBindViewHolder(
        holder: CellViewHolder,
        position: Int,
        payloads: MutableList<Any> ) {

        if(payloads.size != 1 || payloads[0] !== PAYLOAD_SELECTED_CELL_CHANGED) {
            return onBindViewHolder(holder, position)
        }

        setViewHolderSelection(holder, position)
    }


    fun selectCellAt(index: Int) {
        if(positionSelected != index) {
            positionSelected = index
            notifyItemRangeChanged(0, itemCount, PAYLOAD_SELECTED_CELL_CHANGED)
        }
    }


    private fun setViewHolderSelection(holder: CellViewHolder, position: Int) {
        holder.setSelection(position == positionSelected || positionSelected < 0)
    }


    companion object {
        private val PAYLOAD_SELECTED_CELL_CHANGED = Any()
    }
}