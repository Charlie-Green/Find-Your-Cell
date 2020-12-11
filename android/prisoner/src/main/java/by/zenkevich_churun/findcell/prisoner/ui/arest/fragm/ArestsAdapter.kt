package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.core.entity.arest.Arest
import by.zenkevich_churun.findcell.prisoner.R
import kotlinx.android.synthetic.main.arest_item.view.*
import java.text.SimpleDateFormat
import java.util.Calendar


class ArestsAdapter: RecyclerView.Adapter<ArestsAdapter.ArestViewHolder>() {

    private var arests = listOf<Arest>()


    class ArestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val txtvStart = itemView.txtvStart
        private val txtvEnd   = itemView.txtvEnd
        private val txtvJails = itemView.txtvJails


        fun bind(arest: Arest) {
            txtvStart.text = formatDate(arest.start)
            txtvEnd.text = formatDate(arest.end)
            txtvJails.text = ArestUiUtil.jailsText(arest.jails)
        }


        private fun formatDate(cal: Calendar): String {
            return dateFormat.format(cal.time)
        }
    }


    override fun getItemCount(): Int
        = arests.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArestViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.arest_item, parent, false)

        return ArestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArestViewHolder, position: Int) {
        holder.bind(arests[position])
    }


    fun submitList(list: List<Arest>) {
        arests = list
        notifyDataSetChanged()
    }


    companion object {
        @SuppressLint("SimpleDateFormat")  // Only digits, so no need for Locale.
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    }
}