package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import by.zenkevich_churun.findcell.prisoner.util.view.cell.CellView


internal class CellsAdapter(
    private val cells: List<CellModel>
): RecyclerView.Adapter<CellsAdapter.CellViewHolder>() {

    class CellViewHolder(
        private val cellView: CellView
    ): RecyclerView.ViewHolder(cellView) {

        fun bind(cell: CellModel) {
            cellView.apply {
                setBackgroundColor(cell.backColor)
                setNumberBackgroundColor(cell.numberBackColor)
                setTextColor(cell.textColor)
                show(cell)
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
    }
}