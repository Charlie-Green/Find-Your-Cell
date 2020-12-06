package by.zenkevich_churun.findcell.prisoner.util.view.cell

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.R
import kotlinx.android.synthetic.main.cell_view.view.*


/** Compund [View] representing a [Cell]. **/
class CellView: LinearLayout {

    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        View.inflate(context, R.layout.cell_view, this)
        setBackgroundResource(R.drawable.shape_cellview_back)
    }


    override fun setBackgroundColor(color: Int) {
        setViewBackgroundColor(this, color)
    }


    fun setNumberBackgroundColor(color: Int) {
        setViewBackgroundColor(txtvNumber, color)
    }

    fun setTextColor(color: Int) {
        txtvNumber.setTextColor(color)
        txtvJail.setTextColor(color)
    }

    fun show(cell: Cell) {
        txtvNumber.text = cell.number.toString()
        txtvJail.text = cell.jailName
    }


    private fun setViewBackgroundColor(view: View, color: Int) {
        view.background = view.background.mutate().apply {
            setTint(color)
        }
    }
}