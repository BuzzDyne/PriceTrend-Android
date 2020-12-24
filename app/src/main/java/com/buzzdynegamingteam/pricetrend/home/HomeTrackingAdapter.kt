package com.buzzdynegamingteam.pricetrend.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.models.Tracking

class HomeTrackingAdapterOld(private val menuList: List<HomeTrackingItem>) : RecyclerView.Adapter<HomeTrackingAdapterOld.HomeTrackingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTrackingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_list_item, parent, false)

        return HomeTrackingViewHolder(
                itemView
        )
    }

    override fun onBindViewHolder(holder: HomeTrackingViewHolder, position: Int) {
        val currItem = menuList[position]

        holder.imageView.setImageResource(currItem.imageRes)
        holder.nameText.text = currItem.prodName
        holder.priceText.text = currItem.price
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class HomeTrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.prod_image)
        val nameText: TextView = itemView.findViewById(R.id.prodname_text)
        val priceText: TextView = itemView.findViewById(R.id.pricediff_text)
    }
}

class HomeTrackingAdapter() : RecyclerView.Adapter<HomeTrackingAdapter.HomeTrackingViewHolder>() {

    private var itemList : List<Tracking> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTrackingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_list_item, parent, false)
        return HomeTrackingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeTrackingViewHolder, position: Int) {
        val currItem = itemList[position]

        holder.nameTextView.text = currItem.listing?.listingName
        holder.priceText.text = currItem.listing?.latestData?.price.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setTrackings(listOfTracking: List<Tracking>) {
        itemList = listOfTracking
        notifyDataSetChanged()
    }

    class HomeTrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.prodname_text)
        val priceText: TextView = itemView.findViewById(R.id.pricediff_text)

        init {
            itemView.setOnClickListener {
                
            }
        }
    }
}