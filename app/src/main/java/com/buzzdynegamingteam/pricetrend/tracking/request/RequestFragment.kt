package com.buzzdynegamingteam.pricetrend.tracking.request

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.RequestFragmentBinding

class RequestFragment : Fragment() {

    private val TAG = "RequestFragment"
    private lateinit var viewModel: RequestViewModel
    private lateinit var requestAdapter: RequestAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<RequestFragmentBinding>(inflater, R.layout.request_fragment, container, false)

        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
        requestAdapter = RequestAdapter()

        bind.recyclerRequest.adapter = requestAdapter
        bind.recyclerRequest.layoutManager = LinearLayoutManager(this.context)
        bind.recyclerRequest.setHasFixedSize(false)

        viewModel.getRequestList.observe(viewLifecycleOwner, Observer { list ->
            requestAdapter.setRequests(list)
        })

        viewModel.getUpdatingData.observe(viewLifecycleOwner, Observer { isLoading ->
            bind.swipeRefresh.isRefreshing = isLoading
        })

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.loadNewRequestList()
        }

        return bind.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNewRequestList()
    }
}