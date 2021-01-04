package by.zenkevich_churun.findcell.prisoner.util.view.cell

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.CellViewBinding


/** Compund [View] representing a [Cell]. **/
class CellView: LinearLayout {

    private val vb: CellViewBinding


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        val inflater = LayoutInflater.from(context)
        vb = CellViewBinding.inflate(inflater, this)
        setBackgroundResource(R.drawable.shape_roundrect_8)
    }


    override fun setBackgroundColor(color: Int) {
        setViewBackgroundColor(this, color)
    }


    fun setNumberBackgroundColor(color: Int) {
        setViewBackgroundColor(vb.txtvNumber, color)
    }

    fun setTextColor(color: Int) {
        vb.txtvNumber.setTextColor(color)
        vb.txtvJail.setTextColor(color)
    }

    fun show(cell: Cell) {
        vb.txtvNumber.text = cell.number.toString()
        vb.txtvJail.text = cell.jailName
    }


    private fun setViewBackgroundColor(view: View, color: Int) {
        view.background = view.background.mutate().apply {
            setTint(color)
        }
    }
}