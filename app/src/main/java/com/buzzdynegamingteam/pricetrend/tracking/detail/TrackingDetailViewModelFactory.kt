package com.buzzdynegamingteam.pricetrend.tracking.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TrackingDetailViewModelFactory(private val listingDocID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrackingDetailViewModel::class.java)) {
            return TrackingDetailViewModel(listingDocID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}