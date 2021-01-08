package com.buzzdynegamingteam.pricetrend.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val TAG = "SearchViewModel"
    private val repo = SearchRepository


    private val _resultList = MutableLiveData<List<Listing>>()
    val getResultList : LiveData<List<Listing>>
        get() = _resultList


    fun getListingsByTag(tag: String) {
        _resultList.value = listOf()

        viewModelScope.launch {
            _resultList.value = repo.getListingsByTag(tag)
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
}