package com.buzzdynegamingteam.pricetrend.search.detail

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
import android.view.ViewParent
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatDateToString
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatPriceToRupiah
import com.buzzdynegamingteam.pricetrend.common.graphing.CustomMarkerView
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.databinding.SearchDetailFragmentBinding
import com.buzzdynegamingteam.pricetrend.databinding.TrackingDetailFragmentBinding
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.squareup.picasso.Picasso
import kotlin.math.abs

class SearchDetailFragment : Fragment() {

    private val TAG = "SearchDetailFragment"
    private val args : SearchDetailFragmentArgs by navArgs()

    private lateinit var viewModel: SearchDetailViewModel
    private lateinit var viewModelFactory: SearchDetailViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bind = DataBindingUtil.inflate<SearchDetailFragmentBinding>(inflater, R.layout.search_detail_fragment,
            container, false)

        viewModelFactory = SearchDetailViewModelFactory(args.listingDocID)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchDetailViewModel::class.java)

        bind.viewmodel = viewModel
        bind.lifecycleOwner = viewLifecycleOwner

        viewModel.getListingData.observe(viewLifecycleOwner, Observer { listing ->
            bind.textListingPrice.text = formatPriceToRupiah(listing.latestData?.price)
            bind.textListingPriceDiff.text = ""

            Picasso.get()
                .load(listing.listingThumbUrl)
                .placeholder(R.drawable.z650x500)
                .into(bind.imgListing)

            val latestDataTs = formatDateToString(listing.latestData?.ts)
            bind.textTrackedSince.text = getString(R.string.last_updated_format, latestDataTs)
        })

        viewModel.getListingDataRows.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onCreateView: getListingDataRows observer call: $it")
            updatePriceGraph(bind, it)
        })

        viewModel.getSpinnerState.observe(viewLifecycleOwner, Observer { state ->
            updateDataGraph(bind, viewModel.getListingDataRows.value!!, state)
        })

        bind.spinnerLineChart2.adapter = ArrayAdapter<GraphSpinnerState>(requireContext(),
            android.R.layout.simple_spinner_item, GraphSpinnerState.values() )

        bind.spinnerLineChart2.setSelection(Adapter.NO_SELECTION, true)
        bind.spinnerLineChart2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val selected = GraphSpinnerState.values()[position]
                Log.e(TAG, "onItemSelected: position $position, state: $selected")
                viewModel.setSpinnerState(selected)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun updatePriceGraph(bind: SearchDetailFragmentBinding, dataRows: List<Data>) {
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

        //Calculate Max and Min for Y Axis
        var maxValue = Float.MIN_VALUE
        var minValue = Float.MAX_VALUE

        for(dataRow in values) {
            if(dataRow.y > maxValue) maxValue = dataRow.y
            if(dataRow.y < minValue) minValue = dataRow.y
        }

        val padding = if(maxValue.equals(minValue)) maxValue*0.1f else abs(maxValue - minValue) *0.1f

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

    private fun updateDataGraph(bind: SearchDetailFragmentBinding, dataRows: List<Data>, state: GraphSpinnerState) {

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

        //Calculate Max and Min for Y Axis
        var maxValue = Float.MIN_VALUE
        var minValue = Float.MAX_VALUE

        for(dataRow in values) {
            if(dataRow.y > maxValue) maxValue = dataRow.y
            if(dataRow.y < minValue) minValue = dataRow.y
        }

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

}