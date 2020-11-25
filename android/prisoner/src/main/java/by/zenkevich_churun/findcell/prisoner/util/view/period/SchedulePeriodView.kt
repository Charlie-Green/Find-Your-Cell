package by.zenkevich_churun.findcell.prisoner.util.view.period

import android.content.Context
import android.util.*
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.SchedulePeriodModel
import java.text.SimpleDateFormat
import java.util.Calendar


/** Compound [View] to display a [SchedulePeriodModel]. **/
class SchedulePeriodView: LinearLayout {

    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        View.inflate(context, R.layout.period_view, this)
        orientation = VERTICAL
        setPadding( dimen(R.dimen.period_view_padding) )
        setBackgroundResource(R.drawable.shape_cellview_back)
    }


    override fun setBackgroundColor(color: Int) {
        background = background.mutate().apply {
            setTint(color)
        }
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        throw NotImplementedError(
            "Forbidden, since it screws up the implementation of listenToResize" )
    }


    fun show(period: SchedulePeriodModel, cell: CellModel) {
        setBackgroundColor(cell.backColor)

        val day = period.startDate.clone() as Calendar
        var index = 0
        while(!period.endDate.before(day)) {
            val textView = getChildAt(index) as TextView? ?: createTextView().also {
                val params = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                addView(it, params)
            }

            textView.setTextColor(cell.textColor)
            textView.text = "${dateFormat.format(day.time)}: $cell"

            day.add(Calendar.DATE, 1)
            ++index
        }

        if(childCount > index) {
            removeViews(index, childCount-index)
        }
    }

    fun listenToResize(listener: SchedulePeriodResizedListener) {
        val heightDelta = dimen(R.dimen.period_view_height_delta)
        val touchListener = SchedulePeriodTouchListener(listener, heightDelta)
        super.setOnTouchListener(touchListener)
    }


    private fun dimen(dimenRes: Int): Int {
        return context.resources.getDimensionPixelSize(dimenRes)
    }

    private fun createTextView(): TextView {
        return TextView(context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }
    }


    companion object {
        private val dateFormat = SimpleDateFormat("dd.MM.YYYY")
    }
}