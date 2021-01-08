package com.buzzdynegamingteam.pricetrend.search.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.graphing.GraphSpinnerState
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import kotlinx.coroutines.launch

class SearchDetailViewModel(private val listingDocID: String) : ViewModel() {
    private val TAG = "SearchDetailViewModel"

    private val repo = CommonRepository

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
            _listingData.value      = repo.getListingDoc(listingDocID)
            _listingDataRows.value  = repo.getListingDataRows(listingDocID, 7)
            _spinnerState.value     = GraphSpinnerState.SOLD
            _isPageUpdating.value   = false
        }
    }

    fun setSpinnerState(state: GraphSpinnerState) { _spinnerState.value = state }
}