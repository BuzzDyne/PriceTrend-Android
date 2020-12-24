package com.buzzdynegamingteam.pricetrend.tracking.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.TrackingDetailFragmentBinding

class TrackingDetailFragment : Fragment() {

    private lateinit var viewModel: TrackingDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<TrackingDetailFragmentBinding>(inflater, R.layout.tracking_detail_fragment,
            container, false)

        viewModel = ViewModelProvider(this).get(TrackingDetailViewModel::class.java)
        bind.viewmodel = viewModel
        bind.lifecycleOwner = viewLifecycleOwner


        return inflater.inflate(R.layout.tracking_detail_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(TrackingDetailViewModel::class.java)
//    }

}