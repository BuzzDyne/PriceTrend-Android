package com.buzzdynegamingteam.pricetrend.tracking.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import kotlinx.coroutines.launch

class TrackingListViewModel : ViewModel() {
    private val repo = TrackingListRepository

    private val _trackingList = MutableLiveData<List<Tracking>>()
    val getTrackingList : LiveData<List<Tracking>>
        get() = _trackingList

    private val _updatingTrackingList = MutableLiveData<Boolean>()
    val getUpdatingTrackingList : LiveData<Boolean>
        get() = _updatingTrackingList

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


}