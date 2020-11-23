package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.core.util.view.contact.ContactView
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.util.view.add_contact.ContactTypesScrollView
import kotlinx.android.synthetic.main.profile_scrollview_constpart.view.*


/** Adapter for the [RecyclerView] which is the scrollable part of Profile
  * the screen. Includes contacts, [ContactTypesScrollView] and [Prisoner] info. **/
internal class ProfileRecyclerAdapter(
    private val prisoner: Prisoner,
    private val addedContactTypes: List<Contact.Type>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** 1 Contact. **/
    class ContactViewHolder(
        private val contactView: ContactView
    ): RecyclerView.ViewHolder(contactView) {

        fun bind(item: Contact) {
            contactView.show(item)
        }
    }

    /** Constant part. **/
    class ConstantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val addContactView = itemView.addContactView
        private val etInfo = itemView.etInfo

        init {
            addContactView.setContactTypeSelectedListener { type ->
                // ...
            }
        }

        fun bind(prisonerInfo: CharSequence, addedContactTypes: List<Contact.Type>) {
            etInfo.setText(prisonerInfo)
            addContactView.setContent(addedContactTypes)
        }
    }


    override fun getItemCount(): Int
        = prisoner.contacts.size + 1  // + constant part

    override fun getItemViewType(position: Int): Int {
        val isContact = position in prisoner.contacts.indices
        return if(isContact) VIEWTYPE_CONTACT else VIEWTYPE_CONST
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val res = parent.context.resources
        val params = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            bottomMargin = res.getDimensionPixelSize(R.dimen.contact_item_margin)
        }

        return instantiateViewHolder(parent, viewType).apply {
            itemView.layoutParams = params

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position in prisoner.contacts.indices) {
            (holder as ContactViewHolder).bind(prisoner.contacts[position])
        } else {
            (holder as ConstantViewHolder).bind(prisoner.info, addedContactTypes)
        }
    }


    private fun instantiateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        if(type == VIEWTYPE_CONTACT) {
            val contactView = ContactView(parent.context)
            return ContactViewHolder(contactView)
        }

        val constView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.profile_scrollview_constpart, parent, false)
        return ConstantViewHolder(constView)
    }


    companion object {
        private const val VIEWTYPE_CONTACT = 1
        private const val VIEWTYPE_CONST   = 2
    }
}