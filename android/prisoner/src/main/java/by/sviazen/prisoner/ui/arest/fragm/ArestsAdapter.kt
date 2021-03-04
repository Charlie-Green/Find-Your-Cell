package by.sviazen.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.util.LayoutDirection
import android.view.*
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import by.sviazen.domain.entity.Arest
import by.sviazen.prisoner.R
import by.sviazen.prisoner.databinding.ArestItemBinding
import by.sviazen.prisoner.ui.arest.vm.ArestsViewModel


internal class ArestsAdapter(
    private val vm: ArestsViewModel
): RecyclerView.Adapter<ArestsAdapter.ArestViewHolder>() {

    private var arests: List<Arest>? = null
    private var checks: HashSet<Int>? = null
    private var checkable = false
    private var checkableSetCount = 0


    inner class ArestViewHolder(
        private val vm: ArestsViewModel,
        private val vb: ArestItemBinding
    ): RecyclerView.ViewHolder(vb.root) {

        init {
            itemView.setOnClickListener {
                openSchedule()
            }

            itemView.setOnLongClickListener {
                vm.makeCheckable()
                true
            }

            vb.chbDelete.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(isChecked)
            }
        }


        fun bind(arest: Arest) {
            vb.txtvStart.text = ArestUiUtil.format(arest.start)
            vb.txtvEnd.text   = ArestUiUtil.format(arest.end)
            vb.txtvJails.text = ArestUiUtil.jailsText(arest.jails)
            vb.chbDelete.isChecked = checks?.contains(arest.id) ?: false

            setCheckable(false)
        }

        fun setCheckable(animate: Boolean) {
            val affectedViews =
                if(isLayoutExpanded) sequenceOf(vb.txtvEnd, vb.txtvJails)
                else sequenceOf(vb.txtvStart, vb.txtvEnd, vb.txtvJails)

            val desiredTranslate = if(checkable) {
                val res = itemView.context.resources
                -1f * res.getDimensionPixelSize(R.dimen.delete_arest_checkbox_width)
            } else {
                0f
            }

            if(animate) {
                animateCheckBox(affectedViews, desiredTranslate)
            } else {
                translateCheckBox(affectedViews, desiredTranslate)
            }

            itemView
        }


        private val isLayoutExpanded: Boolean
            get() = when(val columnCount = vb.grltRoot.columnCount) {
                2 -> false
                4 -> true
                else -> throw Error("Cannot determine layout for column count $columnCount")
            }

        private fun animateCheckBox(
            animatedViews: Sequence<View>,
            desiredTranslate: Float ) {

            val res = itemView.context.resources
            val duration = res.getInteger(R.integer.arest_delete_animation_duration).toLong()

            ValueAnimator.ofFloat(vb.chbDelete.translationX, desiredTranslate)
                .setDuration(duration)
                .apply { addUpdateListener { animer ->
                    val translate = animer.animatedValue as Float
                    vb.chbDelete.translationX = translate

                    val pad = -translate.toInt()
                    for(v in animatedViews) {
                        setEndPadding(v, pad)
                    }

                }}.start()
        }

        private fun translateCheckBox(
            affectedViews: Sequence<View>,
            desiredTranslate: Float ) {

            vb.chbDelete.translationX = desiredTranslate

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
        val inflater = LayoutInflater.from(parent.context)
        val vb = ArestItemBinding.inflate(inflater, parent, false)
        return ArestViewHolder(vm, vb)
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