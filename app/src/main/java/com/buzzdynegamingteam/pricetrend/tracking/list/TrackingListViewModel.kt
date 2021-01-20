package com.buzzdynegamingteam.pricetrend.tracking.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch

class TrackingListViewModel : ViewModel() {
    private val repo = CommonRepository

    private val _trackingList = MutableLiveData<List<Tracking>>()
    val getTrackingList : LiveData<List<Tracking>>
        get() = _trackingList

    private val _updatingTrackingList = MutableLiveData<Boolean>()
    val getUpdatingTrackingList : LiveData<Boolean>
        get() = _updatingTrackingList

    private val _eventNavigateToRequest = MutableLiveData<Boolean>()
    val getEventNavigateToRequest : LiveData<Boolean>
        get() = _eventNavigateToRequest

    var intentConsumed = false

    init {
        loadNewTrackingData()
    }

    fun loadNewTrackingData() {
        _updatingTrackingList.value = true
        _trackingList.value = listOf()

        viewModelScope.launch {
            _trackingList.value = repo.getListOfUserTrackings().toList()
            _updatingTrackingList.value = false
        }
    }

    fun getTrackingIDfromPos(pos: Int): String? {
        return _trackingList.value?.get(pos)?.documentID
    }

    fun createRequest(url: String) {
        _updatingTrackingList.value = true
        viewModelScope.launch {
            repo.createRequest(url)
            _updatingTrackingList.value = false
        }
    }

    fun createRequestFromIntent(url: String) {
        _updatingTrackingList.value = true
        viewModelScope.launch {
            repo.createRequest(url)
            _updatingTrackingList.value = false
            _eventNavigateToRequest.value = true
        }
    }

    fun onNavigateToRequestFinished() {
        _eventNavigateToRequest.value = false
    }
}