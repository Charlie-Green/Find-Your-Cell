package by.sviazen.core.util.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import by.sviazen.core.databinding.ContactViewBinding
import by.sviazen.core.common.contact.ContactModel
import by.sviazen.domain.entity.Contact


/** A compound [View] to display a [Contact]. **/
class ContactView: LinearLayout {

    private val vb: ContactViewBinding
    private var lastValue: ContactModel? = null


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)


    init {
        val inflater = LayoutInflater.from(context)
        vb = ContactViewBinding.inflate(inflater, this)
    }


    val value: ContactModel?
        get() = lastValue?.apply {
            data = vb.et.text.toString()
        }

    var isEditable: Boolean
        get() = vb.et.isEnabled
        set(value) { vb.et.isEnabled = value }

    fun show(what: Contact) {
        val value = ContactModel
            .from(what)
            .also { lastValue = it }

        vb.imgv.setImageResource(value.iconRes)
        vb.et.setText(value.data)
    }

    fun addOnValueChangedListener(listener: () -> Unit) {

        vb.et.addTextChangedListener { text ->
            val textString = text?.toString() ?: ""

            // Check that this value was not set programmatically:
            if(textString != lastValue?.data) {
                lastValue?.data = textString
                listener()
            }
        }
    }
}