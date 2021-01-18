package com.buzzdynegamingteam.pricetrend.tracking.request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.models.Request

class RequestAdapter : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {
    private var itemList: List<Request> = listOf()
//    private var lastOpenedIndex: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_item, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currItem = itemList[position]

        val title = currItem.url
        val status = currItem.statusCode!!.toInt()

        with(holder) {
            reqTitle.text = title

            when(status) {
                0 -> {
                    reqStatus.text   = itemView.context.getString(R.string.request_stat_0)
                    statusDesc.text  = itemView.context.getString(R.string.request_desc_0)
                    btn.visibility   = View.GONE
                }
                302 -> {
                    reqStatus.text   = itemView.context.getString(R.string.request_stat_302)
                    statusDesc.text  = itemView.context.getString(R.string.request_desc_302)
                    btn.visibility   = View.VISIBLE
                }
                404 -> {
                    reqStatus.text   = itemView.context.getString(R.string.request_stat_404)
                    statusDesc.text  = itemView.context.getString(R.string.request_desc_404)
                    btn.visibility   = View.GONE
                }
                200 -> {
                    reqStatus.text   = itemView.context.getString(R.string.request_stat_200)
                    statusDesc.text  = itemView.context.getString(R.string.request_desc_200)
                    btn.visibility   = View.VISIBLE
                }
                else -> {
                    reqStatus.text   = itemView.context.getString(R.string.request_stat_error)
                    statusDesc.text  = itemView.context.getString(R.string.request_desc_error)
                    btn.visibility   = View.GONE
                }

            }

            val isExpanded = currItem.isExpanded

            expandPart.visibility = if(isExpanded) View.VISIBLE else View.GONE

            mainPart.setOnClickListener {
                currItem.isExpanded = !currItem.isExpanded
                notifyItemChanged(position)
                //Only one allowed to be expandable at a time
//                if(lastOpenedIndex != -1) {
//                    itemList[lastOpenedIndex].isExpanded = false
//                    notifyItemChanged(lastOpenedIndex)
//                }
//
//                currItem.isExpanded = true
//                notifyItemChanged(position)
//                lastOpenedIndex = position
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setRequests(listOfReq: List<Request>) {
        itemList = listOfReq
        notifyDataSetChanged()
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reqTitle: TextView = itemView.findViewById(R.id.text_request_title)
        val reqStatus: TextView = itemView.findViewById(R.id.text_request_status)
        val statusDesc: TextView = itemView.findViewById(R.id.text_status_desc)
        val btn: Button = itemView.findViewById(R.id.btn_req)

        val mainPart: ConstraintLayout = itemView.findViewById(R.id.main_part)
        val expandPart: ConstraintLayout = itemView.findViewById(R.id.expandable_layout)
    }
}