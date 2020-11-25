package by.zenkevich_churun.findcell.prisoner.util.view.cell

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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


    /** Ensure this [CellView] is correctly rendered during a drag-and-drop operation. **/
    fun prepareForShadowBuilder(width: Int, height: Int) {
        val numbWidth = dimen(R.dimen.cellview_number_width)
        val padX      = dimen(R.dimen.cellview_padding_horizontal)
        val padY      = dimen(R.dimen.cellview_padding_vertical)

        txtvNumber.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val textHeight = txtvNumber.measuredHeight
        Log.v("CharlieDebug", "Text height = ${textHeight}")

        layout(0, 0, width, height)
        txtvNumber.layout(padX, padY, padX+numbWidth, height-padY)
        txtvJail.layout(2*padX+numbWidth, (height-textHeight)/2, width-padX, (height+textHeight)/2)
    }


    private fun setViewBackgroundColor(view: View, color: Int) {
        view.background = view.background.mutate().apply {
            setTint(color)
        }
    }

    private fun dimen(dimenRes: Int): Int {
        return context.resources.getDimensionPixelSize(dimenRes)
    }
}