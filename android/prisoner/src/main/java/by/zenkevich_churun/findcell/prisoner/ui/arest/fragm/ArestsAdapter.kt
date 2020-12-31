package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.arest.vm.ArestsViewModel
import kotlinx.android.synthetic.main.arest_item.view.*
import java.text.SimpleDateFormat
import java.util.Calendar


class ArestsAdapter(
    private val vm: ArestsViewModel
): RecyclerView.Adapter<ArestsAdapter.ArestViewHolder>() {

    private var arests = listOf<Arest>()


    class ArestViewHolder(
        private val vm: ArestsViewModel,
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        private val txtvStart = itemView.txtvStart
        private val txtvEnd   = itemView.txtvEnd
        private val txtvJails = itemView.txtvJails


        init {
            itemView.setOnClickListener {
                openSchedule()
            }
        }


        fun bind(arest: Arest) {
            txtvStart.text = formatDate(arest.start)
            txtvEnd.text = formatDate(arest.end)
            txtvJails.text = ArestUiUtil.jailsText(arest.jails)
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


    fun submitList(list: List<Arest>) {
        arests = list
        notifyDataSetChanged()
    }
}