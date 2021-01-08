package com.buzzdynegamingteam.pricetrend.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import com.squareup.picasso.Picasso

class SearchResultAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private var resultList : List<Listing> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_list_item, parent, false)
        return SearchResultViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val currItem = resultList[position]

        holder.nameTextView.text = currItem.listingName
        holder.priceText.text = currItem.latestData?.price.toString()

        Picasso.get()
            .load(currItem.listingThumbUrl)
            .placeholder(R.drawable.z650x500)
            .into(holder.imageView)
    }

    fun setResultList(listOfResult: List<Listing>) {
        resultList = listOfResult
        notifyDataSetChanged()
    }

    inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val nameTextView: TextView = itemView.findViewById(R.id.prodname_text)
        val priceText: TextView = itemView.findViewById(R.id.pricediff_text)
        val imageView: ImageView = itemView.findViewById(R.id.prod_image)

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