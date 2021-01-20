package com.buzzdynegamingteam.pricetrend.tracking.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.TrackingListFragmentBinding

class TrackingListFragment : Fragment(), TrackingListAdapter.OnItemClickListener {

    private val TAG = "TrackingListFragment"
    private lateinit var viewModel: TrackingListViewModel
    private lateinit var trackingAdapter: TrackingListAdapter
    private val args: TrackingListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<TrackingListFragmentBinding>(inflater,R.layout.tracking_list_fragment, container, false)

        viewModel = ViewModelProvider(this).get(TrackingListViewModel::class.java)
        bind.viewmodel = viewModel
        bind.lifecycleOwner = viewLifecycleOwner

        trackingAdapter = TrackingListAdapter(this)
        bind.recyclerTracking.adapter = trackingAdapter
        bind.recyclerTracking.layoutManager = LinearLayoutManager(this.context)
        bind.recyclerTracking.setHasFixedSize(true)


        viewModel.getTrackingList.observe(viewLifecycleOwner, Observer { newTrackingList ->
            trackingAdapter.setTrackings(newTrackingList)
        })

        viewModel.getUpdatingTrackingList.observe(viewLifecycleOwner, Observer { isUpdatingList ->
//            if(!isUpdatingList) {
//                bind.swipeRefresh.isRefreshing = false
//            }
            bind.swipeRefresh.isRefreshing = isUpdatingList
        })

        viewModel.getEventNavigateToRequest.observe(viewLifecycleOwner, Observer { eventNavigate ->
            if(eventNavigate == true) navigateToRequest()
        })

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.loadNewTrackingData()
        }

        bind.filterToolPlaceholder.setOnClickListener {
            it.setBackgroundColor(Color.YELLOW)
        }

        bind.btnFab.setOnClickListener {
            showAddTrackingDialog()
        }

        setHasOptionsMenu(true)

        return bind.root
    }

    override fun onStart() {
        super.onStart()
        if(args.requestUrl != null) { //Link Request Exists
            val link = args.requestUrl!!
            Log.e(TAG, "onCreateView: received url $link")

            if(!viewModel.intentConsumed) { //Link hasn't been processed
                Log.e(TAG, "intentConsumed: ${viewModel.intentConsumed}")
                showAddTrackingFromIntentDialog(link)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNewTrackingData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.e(TAG, "onCreateOptionsMenu: ")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracking_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.goToSavingList -> {
                val action = TrackingListFragmentDirections.actionTrackingListFragmentToTrackingHistoryListFragment()
                findNavController().navigate(action)
                return true
            }
            R.id.goToRequestList -> {
                val action = TrackingListFragmentDirections.actionTrackingListFragmentToRequestFragment()
                findNavController().navigate(action)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(pos: Int) {
        val trackingDocID = viewModel.getTrackingIDfromPos(pos) ?: ""

        Log.e(TAG, "Tracking ID ($trackingDocID) clicked")

        val action = TrackingListFragmentDirections.actionTrackingFragmentToTrackingDetailFragment(trackingDocID)
        findNavController().navigate(action)
    }

    private fun showAddTrackingDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Add new tracking")
        dialog.setMessage("Please enter Listing link.")

        val editText = EditText(requireContext())
        editText.hint = "https://www.tokopedia.com/..."
        dialog.setView(editText)
        dialog.setPositiveButton("Submit", DialogInterface.OnClickListener { dialogg, which ->
            viewModel.createRequest(editText.text.toString())
        })
        dialog.setNegativeButton("Cancel", null)
        dialog.setNeutralButton("Help", null)
        dialog.show()
        editText.requestFocus()
    }

    private fun showAddTrackingFromIntentDialog(url: String) {
        viewModel.intentConsumed = true

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Add new tracking")
        dialog.setMessage("Request a new tracking to be made?\n($url)")

        dialog.setPositiveButton("Confirm", DialogInterface.OnClickListener { _, _ ->
            viewModel.createRequestFromIntent(url)
        })
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _ ->
            Log.e(TAG, "setNegativeButton: ")
        })
        dialog.setOnCancelListener {
            Log.e(TAG, "setOnCancelListener: ")
        }
        dialog.show()
    }

    private fun navigateToRequest() {
        val action = TrackingListFragmentDirections.actionTrackingListFragmentToRequestFragment()
        findNavController().navigate(action)
        viewModel.onNavigateToRequestFinished()
    }
}