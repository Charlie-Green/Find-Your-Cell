package by.zenkevich_churun.findcell.core.util.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import by.zenkevich_churun.findcell.core.R
import by.zenkevich_churun.findcell.core.model.contact.ContactModel
import by.zenkevich_churun.findcell.entity.Contact
import kotlinx.android.synthetic.main.contact_view.view.*


/** A compound [View] to display a [Contact]. **/
class ContactView: LinearLayout {

    private val imgv: ImageView
    private val et: EditText
    private var lastValue: ContactModel? = null


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        val v = View.inflate(context, R.layout.contact_view, this)
        imgv = v.imgv
        et = v.et
    }


    val value: ContactModel?
        get() = lastValue?.apply {
            data = et.text.toString()
        }

    var isEditable: Boolean
        get() = et.isEnabled
        set(value) { et.isEnabled = value }

    fun show(what: Contact) {
        val value = ContactModel
            .from(what)
            .also { lastValue = it }

        imgv.setImageResource(value.iconRes)
        et.setText(value.data)
    }

    fun addOnValueChangedListener(listener: () -> Unit) {

        et.addTextChangedListener { text ->
            val textString = text?.toString() ?: ""

            // Check that this value was not set programmatically:
            if(textString != lastValue?.data) {
                lastValue?.data = textString
                listener()
            }
        }
    }
}