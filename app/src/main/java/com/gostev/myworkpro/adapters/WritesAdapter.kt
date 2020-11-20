package com.gostev.myworkpro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gostev.myworkpro.R
import com.gostev.myworkpro.models.WriteModel
import ru.rambler.libs.swipe_layout.SwipeLayout
import ru.rambler.libs.swipe_layout.SwipeLayout.OnSwipeListener
import kotlinx.android.synthetic.main.write_card.view.*

class WritesAdapter(var items: ArrayList<WriteModel>, var onViewClickListener: OnViewClickListener) : RecyclerView.Adapter<WritesAdapter.ViewHolder>() {

    interface OnViewClickListener {
        fun onClickWrite(view: View, writeModel: WriteModel)
        fun onRemoveWrite(writeModel: WriteModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.write_card, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val writeModel = items[position]

        holder.itemView.card_view.setOnClickListener {
            onViewClickListener.onClickWrite(holder.itemView, writeModel)
        }


        holder.itemView.product_swipe_root.setOnSwipeListener(object : OnSwipeListener {
            override fun onBeginSwipe(swipeLayout: SwipeLayout, moveToRight: Boolean) {
            }

            override fun onSwipeClampReached(swipeLayout: SwipeLayout, moveToRight: Boolean) {
                onViewClickListener.onRemoveWrite(writeModel)
            }

            override fun onLeftStickyEdge(swipeLayout: SwipeLayout, moveToRight: Boolean) {
            }

            override fun onRightStickyEdge(swipeLayout: SwipeLayout, moveToRight: Boolean) {
            }
        })

        holder.itemView.title_card.text = writeModel.getTitle()
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row)
}