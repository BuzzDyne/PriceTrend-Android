package com.buzzdynegamingteam.pricetrend.tracking.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.models.Saving
import kotlinx.coroutines.launch

class SavingListViewModel : ViewModel() {
    private val repo = CommonRepository

    private val _trackingHistoryList = MutableLiveData<List<Saving>>()
    val getTrackingHistoryList : LiveData<List<Saving>>
        get() = _trackingHistoryList

    private val _updatingData = MutableLiveData<Boolean>()
    val getUpdatingData : LiveData<Boolean>
        get() = _updatingData

    init {
        loadNewTrackingHistory()
    }

    fun loadNewTrackingHistory() {
        _updatingData.value = true
        _trackingHistoryList.value = listOf()

        viewModelScope.launch {
            _trackingHistoryList.value = repo.getListOfUserSavings().toList()
            _updatingData.value = false
        }
    }

    fun getTrackingHistoryDocIDfromPos(pos: Int): String? {
        return _trackingHistoryList.value?.get(pos)?.documentId
    }
}