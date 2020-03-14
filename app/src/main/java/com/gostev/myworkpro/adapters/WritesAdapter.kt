package com.gostev.myworkpro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gostev.myworkpro.R
import com.gostev.myworkpro.models.WriteModel
import ru.rambler.libs.swipe_layout.SwipeLayout
import ru.rambler.libs.swipe_layout.SwipeLayout.OnSwipeListener

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
        var writeModel = items[position]

        holder.cardView?.setOnClickListener {
            onViewClickListener.onClickWrite(holder.itemView, writeModel)
        }

        holder.swipeLayout!!.setOnSwipeListener(object : OnSwipeListener {
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

        holder.titleTv?.text = writeModel.getTitle()
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var swipeLayout:SwipeLayout? = null
        var cardView: CardView? = null
        var titleTv: TextView? = null

        init {
            this.swipeLayout = row.findViewById(R.id.product_swipe_root)
            this.cardView = row.findViewById(R.id.card_view)
            this.titleTv = row.findViewById(R.id.title)
        }
    }
}