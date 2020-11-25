package by.zenkevich_churun.findcell.core.util.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import by.zenkevich_churun.findcell.core.R
import by.zenkevich_churun.findcell.core.entity.general.Contact
import kotlinx.android.synthetic.main.contact_view.view.*


/** A compound [View] to display a [Contact]. **/
class ContactView: LinearLayout {

    private val imgv: ImageView
    private val et: EditText
    private var type: Contact.Type? = null
    private var programmaticText: String? = null


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


    val value: Contact?
        get() {
            val data = et.text.toString()
            return when(type) {
                null                   -> null
                Contact.Type.TELEGRAM  -> Contact.Telegram(data)
                Contact.Type.VIBER     -> Contact.Viber(data)
                Contact.Type.VK        -> Contact.VK(data)
                Contact.Type.FACEBOOK  -> Contact.Facebook(data)
                Contact.Type.PHONE     -> Contact.Phone(data)
                Contact.Type.INSTAGRAM -> Contact.Instagram(data)
                Contact.Type.WHATSAPP  -> Contact.WhatsApp(data)
                Contact.Type.SKYPE     -> Contact.Skype(data)
            }
        }

    var isEditable: Boolean
        get() = et.isEnabled
        set(value) { et.isEnabled = value }

    fun show(what: Contact) {
        programmaticText = what.data
        type = what.type

        imgv.setImageResource(what.type.iconRes)
        et.setText(what.data)
    }

    fun addOnValueChangedListener(listener: () -> Unit) {
        et.addTextChangedListener { text ->
            if(text?.toString() != programmaticText) {
                programmaticText = null
                listener()
            }
        }
    }
}