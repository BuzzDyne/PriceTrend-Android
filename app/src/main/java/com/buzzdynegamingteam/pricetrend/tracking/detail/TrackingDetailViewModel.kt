package com.buzzdynegamingteam.pricetrend.tracking.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch

class TrackingDetailViewModel(private val trackingDocID: String) : ViewModel() {
    private val TAG = "TrackingDetailViewModel"

    private val repo = CommonRepository

    private val _trackingData = MutableLiveData<Tracking>()
    val getTrackingData : LiveData<Tracking>
        get() = _trackingData

    private val _listingDataRows = MutableLiveData<List<Data>>()
    val getListingDataRows : LiveData<List<Data>>
        get() = _listingDataRows

    private val _isUpdatingTracking = MutableLiveData<Boolean>()
    val getIsUpdatingTracking : LiveData<Boolean>
        get() = _isUpdatingTracking

    private val _isReadyToPop = MutableLiveData<Boolean>(false)
    val getIsReadyToPop : LiveData<Boolean>
        get() = _isReadyToPop

    private val _spinnerState = MutableLiveData<GraphSpinnerState>()
    val getSpinnerState : LiveData<GraphSpinnerState>
        get() = _spinnerState

    init {
        loadNewTrackingData()
    }

    fun loadNewTrackingData() {
        _isUpdatingTracking.value = true
        viewModelScope.launch {
            _trackingData.value     = repo.getTracking(trackingDocID)
            _listingDataRows.value  = repo.getListingDataRows(_trackingData.value!!.listingDocID!!, 7)
            _spinnerState.value     = GraphSpinnerState.SOLD
            _isUpdatingTracking.value = false
        }
    }

    fun setSpinnerState(state: GraphSpinnerState) { _spinnerState.value = state }

    fun deleteTracking() {
        _isUpdatingTracking.value = true
        viewModelScope.launch {
            repo.deleteTracking(trackingDocID, _trackingData.value?.listingID)
            _isUpdatingTracking.value = false
            _isReadyToPop.value = true
        }
    }
}