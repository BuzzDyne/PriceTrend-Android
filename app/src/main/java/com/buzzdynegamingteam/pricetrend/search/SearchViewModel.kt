package com.buzzdynegamingteam.pricetrend.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val TAG = "SearchViewModel"
    private val repo = CommonRepository

    private val _query = MutableLiveData<String>()
    val getQuery : LiveData<String>
        get() = _query

    private val _resultList = MutableLiveData<List<Listing>>()
    val getResultList : LiveData<List<Listing>>
        get() = _resultList

    fun getListingsByQuery(query: String) {
        _resultList.value = listOf()
        _query.value = query

        val tokens = StringFormatter.queryToTags(query)

        viewModelScope.launch {
            _resultList.value = repo.getListingsByQuery(tokens)
        }
    }

    fun getAllListings() {
        _resultList.value = listOf()

        viewModelScope.launch {
            _resultList.value = repo.getAllListings()
        }
    }

    fun getListingDocIDfromPos(pos: Int): String? {
        return _resultList.value?.get(pos)?.documentId
    }

    fun resetQuery() {
        _query.value = "Search"
    }
}