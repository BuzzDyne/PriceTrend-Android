package com.buzzdynegamingteam.pricetrend.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.HomeFragmentBinding

const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private val safeArgs: HomeFragmentArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<HomeFragmentBinding>(inflater,R.layout.home_fragment,
            container, false)

        setupViews(bind)

        // TODO Set ActionBar Title
//        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home)

        return bind.root
        //return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        Toast.makeText(activity,"Auth is ${safeArgs.isAuthed}",Toast.LENGTH_SHORT).show()
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
        val dummyTrackList  = generateDummyList(10)
        val dummyHotList    = generateDummyList(20)

        fun setupTrackingRecycler(dataList: List<HomeTrackingItem>) {
            bind.trackingRecyclerView.adapter =
                HomeTrackingAdapter(
                    dataList
                )
            bind.trackingRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            //bind.trackingRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
            bind.trackingRecyclerView.setHasFixedSize(true)
        }

        fun setupHotRecycler(dataList: List<HomeTrackingItem>) {
            bind.hotRecyclerView.adapter =
                HomeTrackingAdapter(
                    dataList
                )
            bind.hotRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
            bind.hotRecyclerView.setHasFixedSize(true)

        }

        setupTrackingRecycler(dummyTrackList)
        setupHotRecycler(dummyHotList)

        //OnClickListener
        bind.buttonYourTrackingSeeAll.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_trackingFragment)
        )
    }

}