package com.buzzdynegamingteam.pricetrend.tracking.history.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.TrackingHistoryListFragmentBinding

class TrackingHistoryListFragment : Fragment(), TrackingHistoryListAdapter.OnItemClickListener{

    private val TAG = "TrackingHistoryList"
    private lateinit var viewModel: TrackingHistoryListViewModel
    private lateinit var savingAdapter: TrackingHistoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<TrackingHistoryListFragmentBinding>(inflater, R.layout.tracking_history_list_fragment, container, false)

        viewModel = ViewModelProvider(this).get(TrackingHistoryListViewModel::class.java)
        savingAdapter = TrackingHistoryListAdapter(this)

        bind.recyclerTracking.adapter = savingAdapter
        bind.recyclerTracking.layoutManager = LinearLayoutManager(this.context)
        bind.recyclerTracking.setHasFixedSize(true)

        viewModel.getTrackingHistoryList.observe(viewLifecycleOwner, Observer { list ->
          savingAdapter.setSavings(list)
        })

        viewModel.getUpdatingData.observe(viewLifecycleOwner, Observer { isLoading ->
            bind.swipeRefresh.isRefreshing = isLoading
        })

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.loadNewTrackingHistory()
        }


        return bind.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNewTrackingHistory()
    }

    override fun onItemClick(position: Int) {
        val trackingHistoryDocID = viewModel.getTrackingHistoryDocIDfromPos(position)
        Toast.makeText(requireContext(), "$trackingHistoryDocID clicked!", Toast.LENGTH_SHORT).show()
    }


}