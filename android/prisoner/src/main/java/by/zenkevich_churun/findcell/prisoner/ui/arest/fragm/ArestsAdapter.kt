package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.util.LayoutDirection
import android.view.*
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.arest.vm.ArestsViewModel
import kotlinx.android.synthetic.main.arest_item.view.*
import java.util.Calendar


class ArestsAdapter(
    private val vm: ArestsViewModel
): RecyclerView.Adapter<ArestsAdapter.ArestViewHolder>() {

    private var arests = listOf<Arest>()
    private var checkable = false


    class ArestViewHolder(
        private val vm: ArestsViewModel,
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        private val grltRoot  = itemView.grltRoot
        private val txtvStart = itemView.txtvStart
        private val txtvEnd   = itemView.txtvEnd
        private val txtvJails = itemView.txtvJails
        private val chbDelete = itemView.chbDelete


        init {
            itemView.setOnClickListener {
                openSchedule()
            }

            itemView.setOnLongClickListener {
                // TODO: Do it via ViewModel
                setCheckable(true)
                true
            }
        }


        fun bind(arest: Arest) {
            txtvStart.text = formatDate(arest.start)
            txtvEnd.text   = formatDate(arest.end)
            txtvJails.text = ArestUiUtil.jailsText(arest.jails)
        }

        fun setCheckable(checkable: Boolean) {
            val animatedTextViews =
                if(isLayoutExpanded) sequenceOf(txtvEnd, txtvJails)
                else sequenceOf(txtvStart, txtvEnd,txtvJails)

            if(checkable) {
                translateIn(animatedTextViews)
            } else {
                translateOut()
            }
        }


        private val isLayoutExpanded: Boolean
            get() = when(val columnCount = grltRoot.columnCount) {
                2 -> false
                4 -> true
                else -> throw Error("Cannot determine layout for column count $columnCount")
            }

        private fun translateIn(
            animatedViews: Sequence<View> ) {

            val res = itemView.context.resources
            val desiredTranslate = res
                .getDimensionPixelSize(R.dimen.delete_arest_checkbox_width)
                .times(-1f)

            ValueAnimator.ofFloat(chbDelete.translationX, desiredTranslate)
                .setDuration(2000L)
                .apply { addUpdateListener { animer ->
                    val translate = animer.animatedValue as Float
                    chbDelete.translationX = translate

                    val pad = -translate.toInt()
                    for(v in animatedViews) {
                        setEndPadding(v, pad)
                    }

                }}.start()
        }

        private fun translateOut() {
            // TODO
        }

        private fun setEndPadding(target: View, padding: Int) {
            if(target.layoutDirection == LayoutDirection.RTL) {
                target.updatePadding(left = padding)
            } else {
                target.updatePadding(right = padding)
            }
        }


        private fun openSchedule() {
            vm.openSchedule(adapterPosition)
        }

        private fun formatDate(cal: Calendar): String {
            return ArestUiUtil.format(cal)
        }
    }


    override fun getItemCount(): Int
        = arests.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArestViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.arest_item, parent, false)

        return ArestViewHolder(vm, view)
    }

    override fun onBindViewHolder(holder: ArestViewHolder, position: Int) {
        holder.bind(arests[position])
    }

    override fun onBindViewHolder(
        holder: ArestViewHolder,
        position: Int,
        payloads: MutableList<Any> ) {

        if(payloads.size == 1 &&
            payloads[0] === PAYLOAD_CHECKABLE ) {

            holder.setCheckable(checkable)

        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    var isCheckable: Boolean
        get() { return checkable }
        set(value) {
            if(checkable != value) {
                checkable = value
                notifyItemRangeChanged(0, itemCount, PAYLOAD_CHECKABLE)
            }
        }


    fun submitList(list: List<Arest>) {
        arests = list
        notifyDataSetChanged()
    }


    companion object {
        private val PAYLOAD_CHECKABLE = Any()
    }
}