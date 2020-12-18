package com.buzzdynegamingteam.pricetrend.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.models.User
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val repo = HomeRepository

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val _totalSaving = MutableLiveData<String>()
    val totalSaving : LiveData<String>
        get() = _totalSaving


    init {
        viewModelScope.launch {
            _user.value = repo.getUserData()
            _totalSaving.value = getUserTotalSaving()
        }
//        Log.i("HomeViewModel", "_userValue: ${_user.value}")
    }

    private fun getUserTotalSaving(): String{
        val tSaving = _user.value?.totalSaving
        Log.i(TAG, "_userValue: getUserTotalSaving $tSaving")

        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("IDR")

        return format.format(tSaving)
    }
}