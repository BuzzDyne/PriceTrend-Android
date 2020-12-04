package com.buzzdynegamingteam.pricetrend.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import org.w3c.dom.Text

class TrackingAdapter(private val itemList: List<TrackingItem>) : RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tracking_list_item, parent, false)

        return TrackingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackingViewHolder, position: Int) {
        val currItem = itemList[position]

        holder.imageView.setImageResource(currItem.imageRes)
        holder.nameView.text = currItem.name
        holder.price.text = currItem.price
        holder.priceDiff.text = currItem.priceDiff
        holder.ts.text = currItem.ts

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class TrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView:  ImageView = itemView.findViewById(R.id.image_listing)
        val nameView:   TextView = itemView.findViewById(R.id.text_listing_name)
        val price:      TextView = itemView.findViewById(R.id.text_price)
        val priceDiff:  TextView = itemView.findViewById(R.id.text_price_diff)
        val ts:         TextView = itemView.findViewById(R.id.text_ts)
    }
}