package com.buzzdynegamingteam.pricetrend.tracking.history.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
import com.buzzdynegamingteam.pricetrend.common.models.Saving
import com.squareup.picasso.Picasso

class TrackingHistoryListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<TrackingHistoryListAdapter.TrackingHistoryViewHolder>() {

    private var itemList : List<Saving> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tracking_history_list_item, parent, false)
        return TrackingHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackingHistoryViewHolder, position: Int) {
        val currItem = itemList[position]

        val endPrice    = currItem.endData!!.price
        val priceDiff   = endPrice?.minus(currItem.startData!!.price!!)
        val startTs     = currItem.startData!!.ts
        val endTs       = currItem.endData!!.ts

        holder.nameView.text    = currItem.listingName
        holder.price.text       = StringFormatter.formatPriceToRupiah(endPrice)
        holder.priceDiff.text   = StringFormatter.formatPriceToRupiah(priceDiff)
        holder.startTs.text     = StringFormatter.formatDateToString(startTs)
        holder.endTs.text       = StringFormatter.formatDateToString(endTs)

        Picasso.get()
            .load(currItem.listingThumbURL!!)
            .placeholder(R.drawable.z650x500)
            .into(holder.imageView)

        if(priceDiff!! <= 0) {
            holder.priceDiff.setTextColor(Color.GREEN)
        } else {
            holder.priceDiff.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setSavings(listOfSaving: List<Saving>) {
        itemList = listOfSaving
        notifyDataSetChanged()
    }

    inner class TrackingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val imageView: ImageView = itemView.findViewById(R.id.image_listing)
        val nameView: TextView = itemView.findViewById(R.id.text_listing_name)
        val price: TextView = itemView.findViewById(R.id.text_price)
        val priceDiff: TextView = itemView.findViewById(R.id.text_price_diff)
        val startTs: TextView = itemView.findViewById(R.id.text_start_ts)
        val endTs: TextView = itemView.findViewById(R.id.text_finish_ts)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}