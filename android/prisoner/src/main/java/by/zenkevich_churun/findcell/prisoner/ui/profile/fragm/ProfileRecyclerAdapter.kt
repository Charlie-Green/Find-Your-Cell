package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.animation.ValueAnimator
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.core.util.view.contact.ContactView
import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.profile.vm.ProfileViewModel
import by.zenkevich_churun.findcell.prisoner.util.view.add_contact.ContactTypesScrollView
import kotlinx.android.synthetic.main.profile_scrollview_constpart.view.*


/** Adapter for the [RecyclerView] which is the scrollable part of Profile
  * the screen. Includes contacts, [ContactTypesScrollView] and [Prisoner] info. **/
internal class ProfileRecyclerAdapter(
    private val vm: ProfileViewModel,
    prisoner: Prisoner,
    private val addedContactTypes: MutableList<Contact.Type>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val contacts = prisoner.contacts.toMutableList()
    private var info = prisoner.info
    private var lastAddContactWidth = -1


    /** 1 Contact. **/
    inner class ContactViewHolder(
        private val contactView: ContactView
    ): RecyclerView.ViewHolder(contactView) {

        init {
            contactView.isEditable = true
            contactView.addOnValueChangedListener {
                updateContact()
                vm.notifyDataChanged()
            }
        }

        fun bind(item: Contact) {
            contactView.show(item)
        }

        private fun updateContact() {
            if(adapterPosition !in contacts.indices) {
                return
            }

            contactView.value?.also {
                contacts[adapterPosition] = it
            }
        }
    }


    /** Constant part. **/
    inner class ConstantViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        private val addContactView = itemView.addContactView
        private val etInfo = itemView.etInfo

        init {
            etInfo.addTextChangedListener {
                onPrisonerInfoChanged(etInfo.text)
            }

            addContactView.setContactTypeSelectedListener { type ->
                addContact(type)
            }
        }

        fun bind(prisonerInfo: CharSequence, addedContactTypes: List<Contact.Type>) {
            etInfo.setText(prisonerInfo)

            addContactView.setContent(addedContactTypes)
            if(addedContactTypes.isEmpty()) {
                animateWidthTo(0)
            } else if(itemView.width != 0) {
                animateWidthTo(itemView.width)
            }
        }

        private fun onPrisonerInfoChanged(newInfoChars: CharSequence) {
            val newInfo = newInfoChars.toString()
            if(info != newInfo) {
                vm.notifyDataChanged()
            }

            info = newInfo
        }

        private fun animateWidthTo(target: Int) {
            if(lastAddContactWidth < 0) {
                // Can't animate, since lastAnimatedWidth is invalid.
                lastAddContactWidth = target
                addContactView.updateLayoutParams {
                    width = target
                }

                if(target == 0) {
                    addContactView.visibility = View.GONE
                }

                return
            }

            addContactView.visibility = View.VISIBLE
            ValueAnimator.ofInt(lastAddContactWidth, target).apply {
                addUpdateListener { animer ->
                    lastAddContactWidth = animer.animatedValue as Int
                    addContactView.updateLayoutParams {
                        width = lastAddContactWidth
                    }
                }

                if(target == 0) {
                    doOnEnd {
                        addContactView.visibility = View.GONE
                    }
                }

                duration = 700L
                start()
            }
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val deleteCallback = SwipeToDeleteContactCallback { position ->
            val adapter = recyclerView.adapter as ProfileRecyclerAdapter?
            adapter?.notifyContactDeleted(position)
        }

        ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int
        = contacts.size + 1  // + constant part

    override fun getItemViewType(position: Int): Int {
        val isContact = position in contacts.indices
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
        if(position in contacts.indices) {
            (holder as ContactViewHolder).bind(contacts[position])
        } else {
            (holder as ConstantViewHolder).bind(info, addedContactTypes)
        }
    }


    val prisonerInfo: String
        get() = info


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

    private fun addContact(type: Contact.Type) {
        addedContactTypes.removeAll { it == type }
        val contact = vm.createContact(type, contacts)
        contacts.add(contact)

        notifyItemInserted(contacts.lastIndex)  // Added contact.
        notifyItemChanged(contacts.size)        // Changed ContactTypesView.
        vm.notifyDataChanged()
    }

    private fun notifyContactDeleted(position: Int) {
        if(position !in contacts.indices) {
            return
        }

        addedContactTypes.add( contacts[position].type )
        contacts.removeAt(position)
        notifyItemRemoved(position)        // Contact removed.
        notifyItemChanged(contacts.size)   // Added contact types changed.

        vm.notifyDataChanged()
    }


    companion object {
        private const val VIEWTYPE_CONTACT = 1
        private const val VIEWTYPE_CONST   = 2
    }
}