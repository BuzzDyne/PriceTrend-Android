package com.buzzdynegamingteam.pricetrend.tracking.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatDateToString
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatPriceToRupiah
import com.buzzdynegamingteam.pricetrend.databinding.TrackingDetailFragmentBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class TrackingDetailFragment : Fragment() {
    private val TAG = "TrackingDetailFragment"
    //To receive TrackingDocID
    private val args : TrackingDetailFragmentArgs by navArgs()

    private lateinit var viewModel: TrackingDetailViewModel
    private lateinit var viewModelFactory: TrackingDetailViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e(TAG, "onCreateView: arg received (${args.trackingDocID})")

        val bind = DataBindingUtil.inflate<TrackingDetailFragmentBinding>(inflater, R.layout.tracking_detail_fragment,
            container, false)

        viewModelFactory = TrackingDetailViewModelFactory(args.trackingDocID)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(TrackingDetailViewModel::class.java)
//        viewModel = ViewModelProvider(this).get(TrackingDetailViewModel::class.java)

        bind.viewmodel = viewModel
        bind.lifecycleOwner = viewLifecycleOwner

        bind.btnTest.setOnClickListener{
            val rows = viewModel.getListingDataRows.value

            if (rows != null) {
                for(r in rows) {
                    Log.e(TAG, "Datarow: $r")
                }
            }

            updateGraphData(bind)
        }

        bind.btnTokopedia.setOnClickListener{
            openLink(viewModel.getTrackingData.value?.listing?.listingURL)
        }


        viewModel.getTrackingData.observe(viewLifecycleOwner, Observer { tracking ->
            bind.textListingPrice.text = formatPriceToRupiah(tracking.listing?.latestData?.price)

            val diffPrice = tracking.listing!!.latestData!!.price?.minus(tracking.startPrice!!)
            bind.textListingPriceDiff.text = formatPriceToRupiah(diffPrice)

            if (diffPrice != null) {
                if(diffPrice < 0) {
                    bind.textListingPriceDiff.setTextColor(ContextCompat.getColor(requireContext(), R.color.price_green))
                } else {
                    bind.textListingPriceDiff.setTextColor(ContextCompat.getColor(requireContext(), R.color.price_red))
                }
            }


            val trackedSince = formatDateToString(tracking.startDate)
            bind.textTrackedSince.text = getString(R.string.tracked_since_format, trackedSince)

//            updateGraphData(bind)
        })

        with(bind.lineChart) {
            setBackgroundColor(Color.LTGRAY)
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(true)

            xAxis.enableGridDashedLine(10f, 10f, 0f)

            axisRight.isEnabled = false
            axisLeft.enableGridDashedLine(10f, 10f, 0f)

            axisLeft.axisMaximum = 200f
            axisLeft.axisMinimum = -50f
        }

        return bind.root
    }

    private fun updateGraphData(bind: TrackingDetailFragmentBinding) {
        val values = mutableListOf<Entry>()
        var i = 1F
        for(data in viewModel.getListingDataRows.value!!) {
            values.add(Entry(i, 10F))
            i += 1F
        }

        val set1 = LineDataSet(values, "Test Set")
        val dataSets = listOf<ILineDataSet>(set1)
        val data = LineData(dataSets)

        bind.lineChart.data = data

        set1.notifyDataSetChanged()
        bind.lineChart.data.notifyDataChanged()
        bind.lineChart.notifyDataSetChanged()
    }

    private fun openLink(link: String?) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intent.putExtras(b)

        context?.startActivity(intent)
    }
}