package com.buzzdynegamingteam.pricetrend.tracking.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
import com.buzzdynegamingteam.pricetrend.common.models.Tracking

class TrackingListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<TrackingListAdapter.TrackingViewHolder>() {

    private var itemList : List<Tracking> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tracking_list_item, parent, false)

        return TrackingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackingViewHolder, position: Int) {
        val currItem = itemList[position]

        val currPrice   = currItem.listing?.latestData?.price
        val priceDiff   = currPrice?.minus(currItem.startPrice!!)
        val startTs     = currItem.startDate
        val latestTs    = currItem.listing?.latestData?.ts

        //TODO Listing Image
//        holder.imageView.setImageResource(currItem.listing.listingImgURL)
        holder.nameView.text = currItem.listing?.listingName
        holder.price.text = StringFormatter.formatPriceToRupiah(currPrice)
        holder.priceDiff.text = StringFormatter.formatPriceToRupiah(priceDiff)
        holder.ts.text = StringFormatter.formatDateToString(latestTs)

        if(priceDiff!! <= 0) {
            holder.priceDiff.setTextColor(Color.GREEN)
        } else {
            holder.priceDiff.setTextColor(Color.RED)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setTrackings(listOfTracking: List<Tracking>) {
        itemList = listOfTracking
        notifyDataSetChanged()
    }

    inner class TrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val imageView:  ImageView= itemView.findViewById(R.id.image_listing)
        val nameView:   TextView = itemView.findViewById(R.id.text_listing_name)
        val price:      TextView = itemView.findViewById(R.id.text_price)
        val priceDiff:  TextView = itemView.findViewById(R.id.text_price_diff)
        val ts:         TextView = itemView.findViewById(R.id.text_ts)

        init {
//            itemView.setOnClickListener {
//                Navigation.findNavController(itemView).navigate(TrackingListFragmentDirections.actionTrackingFragmentToTrackingDetailFragment())
//            }
//            imageView.setOnClickListener(this)
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