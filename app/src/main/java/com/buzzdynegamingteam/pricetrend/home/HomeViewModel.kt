package com.buzzdynegamingteam.pricetrend.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.models.User
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.text.NumberFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val repo = HomeRepository

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    //https://stackoverflow.com/questions/63512783/data-binding-how-to-solve-could-not-find-accessor-issue

    private val _totalSaving = MutableLiveData<String>()
    val getTotalSaving : LiveData<String>
        get() = _totalSaving

    private val _trxCount = MutableLiveData<String>()
    val getTrxCount : LiveData<String>
        get() = _trxCount


    init {
        viewModelScope.launch {
            _user.value = repo.getUserData()
            _totalSaving.value = getUserTotalSaving()
            _trxCount.value = getTrxCount()
        }
//        Log.i("HomeViewModel", "_userValue: ${_user.value}")
    }

    private fun getUserTotalSaving(): String{
        val tSaving = _user.value?.totalSaving
        val format = NumberFormat.getCurrencyInstance()
        Log.i(TAG, "getUserTotalSaving: $tSaving")
        
        return try {
            format.currency = Currency.getInstance("IDR")
            format.format(tSaving)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getUserTotalSaving: tSaving is null", e)
            "----"
        } catch (e: Exception) {
            Log.e(TAG, "getUserTotalSaving: Unknown Error", e)
            "ERROR"
        }
    }

    private fun getTrxCount(): String{
        Log.i(TAG, "getTrxCount: ${_user.value?.trxCount}")
        return try {
            _user.value?.trxCount.toString()
        } catch (e: Exception) {
            Log.e(TAG, "getTrxCount: unkown error", e)
            "x"
        }
    }
}