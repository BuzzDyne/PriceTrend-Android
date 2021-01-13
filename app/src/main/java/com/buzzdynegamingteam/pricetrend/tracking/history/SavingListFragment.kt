package com.buzzdynegamingteam.pricetrend.tracking.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.SavingListFragmentBinding

class SavingListFragment : Fragment(){

    private val TAG = "SavingListFragment"
    private lateinit var viewModel: SavingListViewModel
    private lateinit var savingAdapter: SavingListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<SavingListFragmentBinding>(inflater, R.layout.saving_list_fragment, container, false)

        viewModel = ViewModelProvider(this).get(SavingListViewModel::class.java)
        savingAdapter = SavingListAdapter()

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
}