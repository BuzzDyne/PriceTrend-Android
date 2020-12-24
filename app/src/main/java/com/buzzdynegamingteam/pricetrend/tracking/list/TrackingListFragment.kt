package com.buzzdynegamingteam.pricetrend.tracking.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.TrackingListFragmentBinding

class TrackingListFragment : Fragment() {

    private val TAG = "TrackingListFragment"
    private lateinit var viewModel: TrackingListViewModel
    private lateinit var trackingAdapter: TrackingListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<TrackingListFragmentBinding>(inflater,R.layout.tracking_list_fragment, container, false)

        viewModel = ViewModelProvider(this).get(TrackingListViewModel::class.java)

        trackingAdapter = TrackingListAdapter()
        bind.recyclerTracking.adapter = trackingAdapter
        bind.recyclerTracking.layoutManager = LinearLayoutManager(this.context)
        bind.recyclerTracking.setHasFixedSize(true)


        viewModel.getTrackingList.observe(viewLifecycleOwner, Observer { newTrackingList ->
            trackingAdapter.setTrackings(newTrackingList)
        })

        viewModel.getUpdatingTrackingList.observe(viewLifecycleOwner, Observer { isUpdatingList ->
//            if(!isUpdatingList) {
//                bind.swipeRefresh.isRefreshing = false
//            }
            bind.swipeRefresh.isRefreshing = isUpdatingList
        })

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.loadNewTrackingData()
        }

        return bind.root
    }
}