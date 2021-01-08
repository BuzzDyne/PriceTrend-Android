package com.buzzdynegamingteam.pricetrend.search.detail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch
import java.util.*

class SearchDetailViewModel(private val listingDocID: String) : ViewModel() {
    private val TAG = "SearchDetailViewModel"

    private val repo = CommonRepository

    private val _activeTrackingIDs = MutableLiveData<List<String>?>()

    private val _isListingBeingTracked = MutableLiveData<Boolean>()
    val getIsListingBeingTracked : LiveData<Boolean>
        get() = _isListingBeingTracked

    private val _listingData = MutableLiveData<Listing>()
    val getListingData : LiveData<Listing>
        get() = _listingData

    private val _listingDataRows = MutableLiveData<List<Data>>()
    val getListingDataRows : LiveData<List<Data>>
        get() = _listingDataRows

    private val _spinnerState = MutableLiveData<GraphSpinnerState>()
    val getSpinnerState : LiveData<GraphSpinnerState>
        get() = _spinnerState

    private val _isPageUpdating = MutableLiveData<Boolean>()
    val getIsPageUpdating : LiveData<Boolean>
        get() = _isPageUpdating

    init {
        loadNewListingData()
    }

    private fun loadNewListingData() {
        _isPageUpdating.value = true
        viewModelScope.launch {
            _activeTrackingIDs.value        = repo.getUserActiveTrackingIDs()
            _listingData.value              = repo.getListingDoc(listingDocID)
            _isListingBeingTracked.value    = _activeTrackingIDs.value?.contains(_listingData.value!!.listingID)!!
            _listingDataRows.value          = repo.getListingDataRows(listingDocID, 7)
            _spinnerState.value             = GraphSpinnerState.SOLD
            _isPageUpdating.value           = false
        }
    }

    fun setSpinnerState(state: GraphSpinnerState) { _spinnerState.value = state }

    fun createTracking() {
        _isPageUpdating.value = true
        val l = _listingData.value!!
        val t = Tracking(null, listingDocID, l.listingID, false,
                    l.latestData?.price, l.latestData?.sold,
                    l.latestData?.seen, l.latestData?.stock,
                    l.latestData?.reviewCount, l.latestData?.reviewScore,
                    Date())

        viewModelScope.launch {
            repo.createTracking(t)
            _activeTrackingIDs.value    = repo.getUserActiveTrackingIDs()
            _isListingBeingTracked.value= _activeTrackingIDs.value?.contains(_listingData.value!!.listingID)!!
            _isPageUpdating.value       = false

        }
    }
}