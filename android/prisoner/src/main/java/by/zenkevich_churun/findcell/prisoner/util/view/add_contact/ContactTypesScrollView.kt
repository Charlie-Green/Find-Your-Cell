package by.zenkevich_churun.findcell.prisoner.util.view.add_contact

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.prisoner.R
import kotlinx.android.synthetic.main.add_contact_view.view.*


/** A compound [View] the user may use to add a [Contact].
  * This is a [HorizontalScrollView] filled with icons for different [Contact] types. **/
class ContactTypesScrollView: FrameLayout {

    private val hlltContactIcons: LinearLayout
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
        imgSize = resources.getDimensionPixelSize(R.dimen.added_contact_size)
        imgMargin = resources.getDimensionPixelSize(R.dimen.added_contact_margin)

        val v = View.inflate(context, R.layout.add_contact_view, this)
        hlltContactIcons = v.hlltContactIcons
    }


    fun setContent(types: List<Contact.Type>) {
        for(j in types.indices) {
            val existingImageView = hlltContactIcons.getChildAt(j) as? ImageView
            val imgv = existingImageView ?: createImageView()

            imgv.setOnClickListener {
                onContactTypeSelected?.invoke(types[j])
            }

            imgv.setImageResource(types[j].iconRes)
        }

        if(childCount > types.size) {
            removeViews(types.size, childCount-types.size)
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

        hlltContactIcons.addView(imgv, params)

        return imgv
    }
}