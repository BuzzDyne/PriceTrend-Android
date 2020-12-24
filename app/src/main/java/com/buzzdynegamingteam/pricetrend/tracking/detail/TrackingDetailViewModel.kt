package com.buzzdynegamingteam.pricetrend.tracking.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch

class TrackingDetailViewModel : ViewModel() {
    private val repo = TrackingDetailRepository

    private val _trackingData = MutableLiveData<Tracking>()
    val getTrackingData : LiveData<Tracking>
        get() = _trackingData

    private val _updatingTracking = MutableLiveData<Boolean>()
    val getUpdatingTracking : LiveData<Boolean>
        get() = _updatingTracking

    init {
        loadNewTrackingData()
    }

    fun loadNewTrackingData() {
        _updatingTracking.value = true
        viewModelScope.launch {
            _trackingData.value = repo.getTracking()
            _updatingTracking.value = false
        }
    }

}