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
class AddContactView: FrameLayout {

    private val hlltContactIcons: LinearLayout


    constructor(context: Context):
        super(context)
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
        super(context, attrs, defStyleAttr)

    init {
        val v = View.inflate(context, R.layout.add_contact_view, this)
        hlltContactIcons = v.hlltContactIcons
    }


    fun set(icons: List<Int>) {
        for(j in icons.indices) {
            val existingImageView = hlltContactIcons.getChildAt(j) as? ImageView
            val imgv = existingImageView ?: createImageView()
            imgv.setImageResource(icons[j])
        }

        if(childCount > icons.size) {
            removeViews(icons.size, childCount-icons.size)
        }
    }


    private fun createImageView(): ImageView {
        val size = resources.getDimensionPixelSize(R.dimen.added_contact_size)
        val margin = resources.getDimensionPixelSize(R.dimen.added_contact_margin)

        val imgv = ImageView(context)
        val params = FrameLayout.LayoutParams(size, size).apply {
            leftMargin = margin
            rightMargin = margin
        }
        hlltContactIcons.addView(imgv, params)

        return imgv
    }
}