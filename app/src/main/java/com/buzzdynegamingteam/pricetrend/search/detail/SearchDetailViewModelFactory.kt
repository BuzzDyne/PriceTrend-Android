package com.buzzdynegamingteam.pricetrend.search.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buzzdynegamingteam.pricetrend.tracking.detail.TrackingDetailViewModel
import java.lang.IllegalArgumentException

class SearchDetailViewModelFactory(private val listingDocID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchDetailViewModel::class.java)) {
            return SearchDetailViewModel(listingDocID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}