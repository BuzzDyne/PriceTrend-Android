package com.buzzdynegamingteam.pricetrend.tracking.detail

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatDateToString
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatPriceToRupiah
import com.buzzdynegamingteam.pricetrend.common.graphing.CustomMarkerView
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import com.buzzdynegamingteam.pricetrend.databinding.TrackingDetailFragmentBinding
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.squareup.picasso.Picasso
import kotlin.math.abs

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

//            Glide.with(this)
//                    .load(tracking.listing!!.listingThumbUrl)
//                    .placeholder(R.drawable.z650x500)
//                    .into(bind.imgListing)

            Picasso.get()
                    .load(tracking.listing!!.listingThumbUrl)
                    .placeholder(R.drawable.z650x500)
                    .into(bind.imgListing)


            val trackedSince = formatDateToString(tracking.startDate)
            bind.textTrackedSince.text = getString(R.string.tracked_since_format, trackedSince)
        })

        viewModel.getListingDataRows.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onCreateView: getListingDataRow observer call: $it}")
            updatePriceGraph(bind, it, viewModel.getTrackingData.value?.startPrice!!)
        })

        viewModel.getSpinnerState.observe(viewLifecycleOwner, Observer { state ->
            updateDataGraph(bind, viewModel.getListingDataRows.value!!, viewModel.getTrackingData.value!!, state)
        })

        viewModel.getIsReadyToPop.observe(viewLifecycleOwner, Observer { isReadyToPop ->
            if(isReadyToPop) {
                navigateUp()
            }
        })

        bind.btnTokopedia.setOnClickListener{
            openLink(viewModel.getTrackingData.value?.listing?.listingURL)
        }

        bind.btnDeleteTracking.setOnClickListener {
            showConfirmationDialog()
        }

        bind.spinnerLineChart2.adapter = ArrayAdapter<GraphSpinnerState>(requireContext(),
                android.R.layout.simple_spinner_item, GraphSpinnerState.values() )

//        bind.spinnerLineChart2.isSelected = false
        bind.spinnerLineChart2.setSelection(Adapter.NO_SELECTION, true)
        bind.spinnerLineChart2.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val selected = GraphSpinnerState.values()[position]
                Log.e(TAG, "onItemSelected: position $position, state: $selected")
                viewModel.setSpinnerState(selected)
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

        }

        with(bind.lineChart) {
            setBackgroundColor(Color.LTGRAY)
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(true)
            setPinchZoom(true)
            xAxis.granularity = 1.0f

            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.spaceMax = 0.5f
            xAxis.spaceMin = 0.5f

            axisRight.isEnabled = false
            axisLeft.enableGridDashedLine(10f, 10f, 0f)
        }

        with(bind.lineChart2) {
            setBackgroundColor(Color.LTGRAY)
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(true)
            setPinchZoom(true)
            xAxis.granularity = 1.0f

            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.spaceMax = 0.5f
            xAxis.spaceMin = 0.5f

            axisRight.isEnabled = false
            axisLeft.enableGridDashedLine(10f, 10f, 0f)
        }

        return bind.root
    }

    private fun updatePriceGraph(bind: TrackingDetailFragmentBinding, dataRows: List<Data>, startPrice: Long) {
        //List<Entry> -> LineDataSet -> ILineDataSet -> LineData
        bind.lineChart.axisLeft.removeAllLimitLines()
        //Reversed to make first element is the oldest data
        val dataRowsRev = dataRows.asReversed()

        val values = mutableListOf<Entry>()
        val startTimeRef = dataRowsRev[0].ts.time.toFloat() //Epoch time in ms
        val listOfLabels = mutableListOf<String>()

        // Add data
        var index = 0f
        for(data in dataRowsRev) {
//            Log.e(TAG, "updateGraphData: ts before after ($startTimeRef) (${data.ts.time.toFloat()})")
            val tsAsFloat = data.ts.time.toFloat() - startTimeRef
            listOfLabels.add(formatDateToString(data.ts, "dd MMM"))

//            values.add(Entry(tsAsFloat, data.price!!.toFloat()))
            values.add(Entry(index, data.price!!.toFloat()))
            index += 1f
        }

        // Add Limit Line
        val startPriceFloat = startPrice.toFloat()
        val llXAxis = LimitLine(startPriceFloat, "Start Price")
        llXAxis.lineWidth = 2f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f
        bind.lineChart.axisLeft.addLimitLine(llXAxis)

        //Calculate Max and Min for Y Axis
        var maxValue = Float.MIN_VALUE
        var minValue = Float.MAX_VALUE

        for(dataRow in values) {
            if(dataRow.y > maxValue) maxValue = dataRow.y
            if(dataRow.y < minValue) minValue = dataRow.y
        }

        if(startPriceFloat > maxValue) maxValue = startPriceFloat
        if(startPriceFloat < minValue) minValue = startPriceFloat


        val padding = if(maxValue.equals(minValue)) maxValue*0.1f else abs(maxValue - minValue)*0.1f

        with(bind.lineChart) {
            axisLeft.axisMaximum = maxValue + padding
            axisLeft.axisMinimum = minValue - padding

            xAxis.valueFormatter = IndexAxisValueFormatter(listOfLabels)
        }

        // Set Data
        val lineDataSet = LineDataSet(values, "Price")
        val iLineDataSet = listOf<ILineDataSet>(lineDataSet)
        val lineData = LineData(iLineDataSet)

        // Refresh Graph
        val mv = CustomMarkerView(requireContext(), R.layout.custom_marker_view, listOfLabels)
        mv.chartView = bind.lineChart
        bind.lineChart.marker = mv

        bind.lineChart.data = lineData
        lineDataSet.notifyDataSetChanged()
        bind.lineChart.data.notifyDataChanged()
        bind.lineChart.notifyDataSetChanged()
        bind.lineChart.invalidate()
    }

    private fun updateDataGraph(bind: TrackingDetailFragmentBinding, dataRows: List<Data>, startData: Tracking, state: GraphSpinnerState) {

        bind.lineChart2.axisLeft.removeAllLimitLines()

        //Reversed to make first element is the oldest data
        val dataRowsRev = dataRows.asReversed()

        val values = mutableListOf<Entry>()
        val startTimeRef = dataRowsRev[0].ts.time.toFloat() //Epoch time in ms
        val listOfLabels = mutableListOf<String>()

        // Add data
        var index = 0f
        for(data in dataRowsRev) {
            listOfLabels.add(formatDateToString(data.ts, "dd MMM"))

            when(state) {
                GraphSpinnerState.REVIEW_COUNT  -> values.add(Entry(index, data.reviewCount!!.toFloat()))
                GraphSpinnerState.REVIEW_SCORE  -> values.add(Entry(index, data.reviewScore!!.toFloat()))
                GraphSpinnerState.SEEN          -> values.add(Entry(index, data.seen!!.toFloat()))
                GraphSpinnerState.STOCK         -> values.add(Entry(index, data.stock!!.toFloat()))
                else                            -> values.add(Entry(index, data.sold!!.toFloat()))
            }

            index += 1f
        }

        // Add Limit Line
        val startDataFloat: Float = when(state) {
            GraphSpinnerState.REVIEW_COUNT  -> startData.startReviewCount?.toFloat()!!
            GraphSpinnerState.REVIEW_SCORE  -> startData.startReviewScore?.toFloat()!!
            GraphSpinnerState.SEEN          -> startData.startSeenCount?.toFloat()!!
            GraphSpinnerState.STOCK         -> startData.startStockCount?.toFloat()!!
            else                            -> startData.startSoldCount?.toFloat()!!
        }
        val llXAxis = LimitLine(startDataFloat, "Start ${state.str}")
        llXAxis.lineWidth = 1f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f
        bind.lineChart2.axisLeft.addLimitLine(llXAxis)

        //Calculate Max and Min for Y Axis
        var maxValue = Float.MIN_VALUE
        var minValue = Float.MAX_VALUE

        for(dataRow in values) {
            if(dataRow.y > maxValue) maxValue = dataRow.y
            if(dataRow.y < minValue) minValue = dataRow.y
        }

        if(startDataFloat > maxValue) maxValue = startDataFloat
        if(startDataFloat < minValue) minValue = startDataFloat

        val padding = if(maxValue.equals(minValue)) maxValue*0.1f else abs(maxValue - minValue)*0.1f

        with(bind.lineChart2) {
            axisLeft.axisMaximum = maxValue + padding
            axisLeft.axisMinimum = minValue - padding

            xAxis.valueFormatter = IndexAxisValueFormatter(listOfLabels)
        }

        // Set Data
        val lineDataSet = LineDataSet(values, state.str)
        val iLineDataSet = listOf<ILineDataSet>(lineDataSet)
        val lineData = LineData(iLineDataSet)

        // Refresh Graph
        val mv = CustomMarkerView(requireContext(), R.layout.custom_marker_view, listOfLabels, state)
        mv.chartView = bind.lineChart2
        bind.lineChart2.marker = mv

        bind.lineChart2.data = lineData
        lineDataSet.notifyDataSetChanged()
        bind.lineChart2.data.notifyDataChanged()
        bind.lineChart2.notifyDataSetChanged()
        bind.lineChart2.invalidate()
    }

    private fun openLink(link: String?) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intent.putExtras(b)

        context?.startActivity(intent)
    }

    private fun showConfirmationDialog() {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Delete Tracking?")
        alert.setMessage("Are you sure you want to delete this tracking?\n(Tracking Data will be lost forever)")

        alert.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ -> // User clicked OK button.
            viewModel.deleteTracking()
        })
        alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> // User cancelled the dialog.

        })

        alert.show()
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }
}