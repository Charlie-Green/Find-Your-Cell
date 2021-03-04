package by.sviazen.prisoner.ui.sched.fragm

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import by.sviazen.prisoner.R
import by.sviazen.prisoner.ui.common.sched.cell.CellModel
import by.sviazen.prisoner.ui.sched.vm.ScheduleViewModel
import by.sviazen.prisoner.util.view.cell.CellView


internal class CellsAdapter(
    private val cells: List<CellModel>,
    private val vm: ScheduleViewModel
): RecyclerView.Adapter<CellsAdapter.CellViewHolder>() {

    private var positionSelected = -1
    private var lastCount = cells.size


    inner class CellViewHolder(
        private val cellView: CellView
    ): RecyclerView.ViewHolder(cellView) {

        init {
            cellView.setOnClickListener {
                vm.swapCellSelection(adapterPosition)
            }
            cellView.setOnLongClickListener {
                vm.requestOptions(adapterPosition)
                true
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
        = cells.size.also { lastCount = it }

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

    fun notifyCellProbablyAdded() {
        if(itemCount == lastCount + 1) {
            notifyItemInserted(lastCount++)
        } else {
            notifyDataSetChanged()
        }
    }


    private fun setViewHolderSelection(holder: CellViewHolder, position: Int) {
        holder.setSelection(position == positionSelected || positionSelected < 0)
    }


    companion object {
        private val PAYLOAD_SELECTED_CELL_CHANGED = Any()
    }
}