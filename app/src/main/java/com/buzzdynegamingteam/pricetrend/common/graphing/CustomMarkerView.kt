package com.buzzdynegamingteam.pricetrend.common.graphing

import android.content.Context
import android.widget.TextView
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.StringFormatter.Companion.formatPriceToRupiah
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int, val listOfLabels: List<String>, val state: GraphSpinnerState? = null) : MarkerView(context, layoutResource) {
    private val tvContent = findViewById<TextView>(R.id.text_markerbox_content)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        val xLabel = listOfLabels[e?.x?.toInt()!!]

        val yLabel : String = if(state == null) {
            //This is for price graph
            formatPriceToRupiah(e.y)
        } else {
            //This is for data graph
            e.y.toString()
        }

        tvContent.text = "${yLabel}\n$xLabel"

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width/2)).toFloat(), (-height).toFloat())
    }
}