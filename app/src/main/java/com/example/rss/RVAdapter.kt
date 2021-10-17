package com.example.rss

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RVAdapter (val itemList : List<Questions>) : RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder  (itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = itemList[position]

        holder.itemView.apply {
            for(i in itemList) {
                tvItem.text = items.title + "\n " + items.name
            }
        }
    }

    override fun getItemCount() = itemList.size
}