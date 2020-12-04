package com.buzzdynegamingteam.pricetrend.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R

class HomeTrackingAdapter(private val menuList: List<HomeTrackingItem>) : RecyclerView.Adapter<HomeTrackingAdapter.HomeTrackingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTrackingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_list_item, parent, false)

        return HomeTrackingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeTrackingViewHolder, position: Int) {
        val currItem = menuList[position]

        holder.imageView.setImageResource(currItem.imageRes)
        holder.nameText.text = currItem.prodName
        holder.priceText.text = currItem.price

//        if(position == 2) {
//            holder.nameText.setBackgroundColor(Color.YELLOW)
//        } else {
//            holder.nameText.setBackgroundColor(Color.TRANSPARENT)
//        }
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