package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.util.LayoutDirection
import android.util.Log
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

    private var arests: List<Arest>? = null
    private var checks: HashSet<Int>? = null
    private var checkable = false
    private var checkableSetCount = 0


    inner class ArestViewHolder(
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
             // TODO: CharlieDebug: Uncomment
            // openSchedule()
            }

            itemView.setOnLongClickListener {
                vm.makeCheckable()
                true
            }

            chbDelete.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(isChecked)
            }
        }


        fun bind(arest: Arest) {
            txtvStart.text = formatDate(arest.start)
            txtvEnd.text   = formatDate(arest.end)
            txtvJails.text = ArestUiUtil.jailsText(arest.jails)
            chbDelete.isChecked = checks?.contains(arest.id) ?: false

            setCheckable(false)
        }

        fun setCheckable(animate: Boolean) {
            val affectedViews =
                if(isLayoutExpanded) sequenceOf(txtvEnd, txtvJails)
                else sequenceOf(txtvStart, txtvEnd,txtvJails)

            val desiredTranslate = if(checkable) {
                val res = itemView.context.resources
                -1f * res.getDimensionPixelSize(R.dimen.delete_arest_checkbox_width)
            } else {
                0f
            }

            if(animate) {
                Log.v("CharlieDebug", "Animate to $desiredTranslate")
                animateCheckBox(affectedViews, desiredTranslate)
            } else {
                Log.v("CharlieDebug", "Translate to $desiredTranslate")
                translateCheckBox(affectedViews, desiredTranslate)
            }

            itemView
        }


        private val isLayoutExpanded: Boolean
            get() = when(val columnCount = grltRoot.columnCount) {
                2 -> false
                4 -> true
                else -> throw Error("Cannot determine layout for column count $columnCount")
            }

        private fun animateCheckBox(
            animatedViews: Sequence<View>,
            desiredTranslate: Float ) {

            val res = itemView.context.resources
            val duration = res.getInteger(R.integer.arest_delete_animation_duration).toLong()

            ValueAnimator.ofFloat(chbDelete.translationX, desiredTranslate)
                .setDuration(duration)
                .apply { addUpdateListener { animer ->
                    val translate = animer.animatedValue as Float
                    chbDelete.translationX = translate

                    val pad = -translate.toInt()
                    for(v in animatedViews) {
                        setEndPadding(v, pad)
                    }

                }}.start()
        }

        private fun translateCheckBox(
            affectedViews: Sequence<View>,
            desiredTranslate: Float ) {

            chbDelete.translationX = desiredTranslate

            val desiredPadding = -desiredTranslate.toInt()
            for(v in affectedViews) {
                setEndPadding(v, desiredPadding)
            }
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


        private fun onCheckedChanged(isChecked: Boolean) {
            val id = arestAt(adapterPosition)?.id ?: return
            if(isChecked) {
                checks?.add(id)
            } else {
                checks?.remove(id)
            }
        }
    }


    override fun getItemCount(): Int
        = arests?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArestViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.arest_item, parent, false)

        return ArestViewHolder(vm, view)
    }

    override fun onBindViewHolder(holder: ArestViewHolder, position: Int) {
        val arest = arestAt(position) ?: return
        holder.bind(arest)
    }

    override fun onBindViewHolder(
        holder: ArestViewHolder,
        position: Int,
        payloads: MutableList<Any> ) {

        if(payloads.size == 1 &&
            payloads[0] === PAYLOAD_CHECKABLE ) {

            holder.setCheckable(checkableSetCount >= 2)

        } else {
            onBindViewHolder(holder, position)
        }
    }


    val checkedIds: HashSet<Int>
        get() = checks ?: hashSetOf()

    var isCheckable: Boolean
        get() { return checkable }
        set(value) {
            ++checkableSetCount
            if(checkable != value) {
                checkable = value
                notifyItemRangeChanged(0, itemCount, PAYLOAD_CHECKABLE)
            }
        }


    fun submitList(list: List<Arest>, checkedIds: HashSet<Int>) {
        arests = list
        checks = checkedIds
        notifyDataSetChanged()
    }


    private fun arestAt(position: Int): Arest? {
        val arests = this.arests ?: return null
        if(position !in arests.indices) {
            return null
        }

        return arests[position]
    }


    companion object {
        private val PAYLOAD_CHECKABLE = Any()
    }
}