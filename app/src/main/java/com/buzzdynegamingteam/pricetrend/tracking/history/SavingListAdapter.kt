package com.buzzdynegamingteam.pricetrend.tracking.history

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatDateToString
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatPriceToRupiah
import com.buzzdynegamingteam.pricetrend.common.models.Saving
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class SavingListAdapter : RecyclerView.Adapter<SavingListAdapter.SavingViewHolder>() {
    private val TAG = "SavingListAdapter"
    private var itemList : List<Saving> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.saving_list_item, parent, false)
        return SavingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavingViewHolder, position: Int) {
        val currItem = itemList[position]

        val endPrice    = currItem.endData!!.price
        val startPrice  = currItem.startData!!.price
        val priceDiff   = endPrice?.minus(startPrice!!)
        val startTs     = currItem.startData!!.ts
        val endTs       = currItem.endData.ts

        holder.nameView.text    = currItem.listingName
        holder.price.text       = formatPriceToRupiah(endPrice)
        holder.priceDiff.text   = formatPriceToRupiah(priceDiff)
        holder.startTs.text     = formatDateToString(startTs)
        holder.endTs.text       = formatDateToString(endTs)

        Picasso.get()
            .load(currItem.listingThumbURL!!)
            .placeholder(R.drawable.z650x500)
            .into(holder.imageView)

        if(priceDiff!! <= 0) {
            holder.priceDiff.setTextColor(Color.GREEN)
        } else {
            holder.priceDiff.setTextColor(Color.RED)
        }

        with(holder){
            tableStartTs.text       = formatDateToString(startTs, "dd MMM YYYY")
            tableEndTs.text         = formatDateToString(endTs, "dd MMM YYYY")

            tableStartPrice.text    = formatPriceToRupiah(startPrice)
            tableEndPrice.text      = formatPriceToRupiah(endPrice)

            tableStartSold.text     = currItem.startData.sold.toString()
            tableEndSold.text       = currItem.endData.sold.toString()

            tableStartStock.text    = currItem.startData.stock.toString()
            tableEndStock.text      = currItem.endData.stock.toString()

            tableStartSeen.text     = currItem.startData.seen.toString()
            tableEndSeen.text       = currItem.endData.seen.toString()

            tableStartReviewCount.text     = currItem.startData.reviewCount.toString()
            tableEndReviewCount.text       = currItem.endData.reviewCount.toString()

            tableStartReviewScore.text     = currItem.startData.reviewScore.toString()
            tableEndReviewScore.text       = currItem.endData.reviewScore.toString()
        }

        val isExpanded = currItem.isExpanded

        holder.expandPart.visibility = if(isExpanded) View.VISIBLE else View.GONE

        holder.mainPart.setOnClickListener {
            currItem.isExpanded = !currItem.isExpanded
            Log.e(TAG, "mainPart.setOnClickListener: after isExpanded = $isExpanded")

            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setSavings(listOfSaving: List<Saving>) {
        itemList = listOfSaving
        notifyDataSetChanged()
    }

    inner class SavingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_listing)
        val nameView: TextView = itemView.findViewById(R.id.text_listing_name)
        val price: TextView = itemView.findViewById(R.id.text_price)
        val priceDiff: TextView = itemView.findViewById(R.id.text_price_diff)
        val startTs: TextView = itemView.findViewById(R.id.text_start_ts)
        val endTs: TextView = itemView.findViewById(R.id.text_finish_ts)

        val root: MaterialCardView = itemView.findViewById(R.id.root_layout)
        val mainPart: ConstraintLayout = itemView.findViewById(R.id.main_part)
        val expandPart: ConstraintLayout = itemView.findViewById(R.id.expandable_layout)

        val tableStartTs: TextView = itemView.findViewById(R.id.table_start_ts)
        val tableEndTs  : TextView = itemView.findViewById(R.id.table_end_ts)

        val tableStartPrice: TextView = itemView.findViewById(R.id.table_start_price)
        val tableEndPrice  : TextView = itemView.findViewById(R.id.table_end_price)

        val tableStartSold: TextView = itemView.findViewById(R.id.table_start_sold)
        val tableEndSold  : TextView = itemView.findViewById(R.id.table_end_sold)

        val tableStartStock: TextView = itemView.findViewById(R.id.table_start_stock)
        val tableEndStock  : TextView = itemView.findViewById(R.id.table_end_stock)

        val tableStartSeen: TextView = itemView.findViewById(R.id.table_start_seen)
        val tableEndSeen  : TextView = itemView.findViewById(R.id.table_end_seen)

        val tableStartReviewCount: TextView = itemView.findViewById(R.id.table_start_review_count)
        val tableEndReviewCount  : TextView = itemView.findViewById(R.id.table_end_review_count)

        val tableStartReviewScore: TextView = itemView.findViewById(R.id.table_start_review_score)
        val tableEndReviewScore  : TextView = itemView.findViewById(R.id.table_end_review_score)


    }
}