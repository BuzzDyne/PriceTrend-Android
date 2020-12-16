package com.buzzdynegamingteam.pricetrend.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.TrackingFragmentBinding


class TrackingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<TrackingFragmentBinding>(inflater,R.layout.tracking_fragment, container, false)


        val dummyTrackingList = generateDummyTrackingList(30)

        bind.recyclerTracking.adapter =
            TrackingAdapter(
                dummyTrackingList
            )
        bind.recyclerTracking.layoutManager = LinearLayoutManager(this.context)
        bind.recyclerTracking.setHasFixedSize(true)

        return bind.root
    }

    private fun generateDummyTrackingList(size: Int): List<TrackingItem> {
        val list = ArrayList<TrackingItem>()

        for (i in 1 until size+1) {
            val drawable = when (i % 5) {
                0 -> R.drawable.camera
                1 -> R.drawable.z100x200
                2 -> R.drawable.z500x500
                3 -> R.drawable.z500x650
                else -> R.drawable.z650x500
            }

            val item = TrackingItem(
                drawable,
                "This is $i very long name This is a very long name long name This long name This long name This ",
                "Rp ${i}0.000",
                "-Rp${i}.000",
                "$i Jun 2020"
            )
            list += item
        }

        return list
    }
}