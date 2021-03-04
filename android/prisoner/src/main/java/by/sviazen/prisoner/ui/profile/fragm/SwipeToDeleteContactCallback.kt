package by.sviazen.prisoner.ui.profile.fragm

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


internal class SwipeToDeleteContactCallback(
    private val onItemDeleted: (position: Int) -> Unit
): ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if(viewHolder !is ProfileRecyclerAdapter.ContactViewHolder) {
            return 0
        }

        return makeFlag(
            ItemTouchHelper.ACTION_STATE_SWIPE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.25f*defaultValue
    }



    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Drag not supported.
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDeleted(viewHolder.adapterPosition)
    }
}