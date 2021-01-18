package com.buzzdynegamingteam.pricetrend.tracking.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.models.Request
import kotlinx.coroutines.launch

class RequestViewModel : ViewModel() {

    private val repo = CommonRepository

    private val _requestList = MutableLiveData<List<Request>>()
    val getRequestList : LiveData<List<Request>>
        get() = _requestList

    private val _updatingData = MutableLiveData<Boolean>()
    val getUpdatingData : LiveData<Boolean>
        get() = _updatingData

    init {
        loadNewRequestList()
    }

    fun loadNewRequestList() {
        _updatingData.value = true
        _requestList.value = listOf()

        viewModelScope.launch {
            _requestList.value = repo.getUserRequests().toList()
            _updatingData.value = false
        }
    }
}