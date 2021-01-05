package com.buzzdynegamingteam.pricetrend.tracking.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch

class TrackingDetailViewModel(private val trackingDocID: String) : ViewModel() {
    private val TAG = "TrackingDetailViewModel"

    private val repo = TrackingDetailRepository

    private val _trackingData = MutableLiveData<Tracking>()
    val getTrackingData : LiveData<Tracking>
        get() = _trackingData

    private val _listingDataRows = MutableLiveData<List<Data>>()
    val getListingDataRows : LiveData<List<Data>>
        get() = _listingDataRows

    private val _updatingTracking = MutableLiveData<Boolean>()
    val getUpdatingTracking : LiveData<Boolean>
        get() = _updatingTracking

    private val _spinnerState = MutableLiveData<GraphSpinnerState>()
    val getSpinnerState : LiveData<GraphSpinnerState>
        get() = _spinnerState

    init {
        loadNewTrackingData()
    }

    fun loadNewTrackingData() {
        _updatingTracking.value = true
        viewModelScope.launch {
            _trackingData.value     = repo.getTracking(trackingDocID)
            _listingDataRows.value  = repo.getListingDataRows(_trackingData.value!!.listingDocID!!, 7)
            _spinnerState.value     = GraphSpinnerState.SOLD
            _updatingTracking.value = false
        }
    }

    fun setSpinnerState(state: GraphSpinnerState) { _spinnerState.value = state }
}