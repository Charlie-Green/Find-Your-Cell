package by.zenkevich_churun.findcell.prisoner.util.view.add_contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import by.zenkevich_churun.findcell.core.common.contact.ContactModel
import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.ContactTypesScrollViewBinding


/** A compound [View] the user may use to add a [Contact].
  * This is a [HorizontalScrollView] filled with icons for different [Contact] types. **/
// TODO: Derive directly from HSV
class ContactTypesScrollView: FrameLayout {

    private val vb: ContactTypesScrollViewBinding
    private val imgSize: Int
    private val imgMargin: Int
    private var onContactTypeSelected: ((Contact.Type) -> Unit)? = null


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)

    init {
        val inflater = LayoutInflater.from(context)
        vb = ContactTypesScrollViewBinding.inflate(inflater, this)
        imgSize = resources.getDimensionPixelSize(R.dimen.added_contact_size)
        imgMargin = resources.getDimensionPixelSize(R.dimen.added_contact_margin)
    }


    fun setContent(types: List<Contact.Type>) {
        for(j in types.indices) {
            val existingImageView = vb.hlltContactIcons.getChildAt(j) as? ImageView
            val imgv = existingImageView ?: createImageView()

            imgv.setOnClickListener {
                onContactTypeSelected?.invoke(types[j])
            }

            val iconRes = ContactModel.iconResourceFor(types[j])
            imgv.setImageResource(iconRes)
        }

        val itemCount = vb.hlltContactIcons.childCount
        if(itemCount > types.size) {
            vb.hlltContactIcons.removeViews(types.size, itemCount-types.size)
        }
    }

    fun setContactTypeSelectedListener(listener: ((Contact.Type) -> Unit)?) {
        onContactTypeSelected = listener
    }


    private fun createImageView(): ImageView {
        val imgv = ImageView(context)
        val params = FrameLayout.LayoutParams(imgSize, imgSize).apply {
            leftMargin = imgMargin
            rightMargin = imgMargin
        }

        vb.hlltContactIcons.addView(imgv, params)

        return imgv
    }
}