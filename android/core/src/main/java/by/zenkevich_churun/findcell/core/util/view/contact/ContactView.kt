package by.zenkevich_churun.findcell.core.util.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import by.zenkevich_churun.findcell.core.R
import by.zenkevich_churun.findcell.core.entity.Contact
import kotlinx.android.synthetic.main.contact_view.view.*


/** A compound [View] to display a [Contact]. **/
class ContactView: LinearLayout {

    private val imgv: ImageView
    private val txtv: TextView


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        val v = View.inflate(context, R.layout.contact_view, this)
        imgv = v.imgv
        txtv = v.txtv
    }


    fun show(what: Contact) {
        imgv.setImageResource(what.type.iconRes)
        txtv.text = what.data
    }
}