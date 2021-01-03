package com.buzzdynegamingteam.pricetrend.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.HomeFragmentBinding

const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var navController: NavController
    private lateinit var homeTrackingAdapter: HomeTrackingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<HomeFragmentBinding>(inflater,R.layout.home_fragment,
            container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        bind.viewmodel = viewModel
        bind.lifecycleOwner = viewLifecycleOwner

        setupViews(bind)

        viewModel.getTrackingList.observe(viewLifecycleOwner, Observer { newTrackingList ->
            homeTrackingAdapter.setTrackings(newTrackingList)
        })

        viewModel.getCurrUser.observe(viewLifecycleOwner, Observer { currUser ->
            if(currUser == null) {
                navigateToLoginScreen()
            }
        })

        return bind.root
    }

    private fun generateDummyList(size: Int): List<HomeTrackingItem> {
        val list = ArrayList<HomeTrackingItem>()

        for (i in 0 until size) {
            val drawable = when (i % 5) {
                0 -> R.drawable.camera
                1 -> R.drawable.z100x200
                2 -> R.drawable.z500x500
                3 -> R.drawable.z500x650
                else -> R.drawable.z650x500
            }

            val item =
                    HomeTrackingItem(
                            drawable,
                            "Product Name $i",
                            "Rp ${i}.000"
                    )
            list += item
        }

        return list
    }

    private fun setupViews(bind: HomeFragmentBinding) {
        val dummyHotList    = generateDummyList(20)

        fun setupTrackingRecycler() {
            homeTrackingAdapter = HomeTrackingAdapter()
            bind.trackingRecyclerView.adapter = homeTrackingAdapter
            bind.trackingRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            bind.trackingRecyclerView.setHasFixedSize(true)
        }

        fun setupHotRecycler(dataList: List<HomeTrackingItem>) {
            bind.hotRecyclerView.adapter =
                    HomeTrackingAdapterOld(
                            dataList
                    )
            bind.hotRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
            bind.hotRecyclerView.setHasFixedSize(true)

        }

        setupTrackingRecycler()
        setupHotRecycler(dummyHotList)

        //OnClickListener
        bind.buttonYourTrackingSeeAll.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_trackingFragment)
        )
    }

    fun onHomeTrackingItemClick(item: HomeTrackingItem) {
        // TODO findNavController().navigate()
    }

    private fun navigateToLoginScreen() {
        Log.e(TAG, "navigateToLoginScreen: ")
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}