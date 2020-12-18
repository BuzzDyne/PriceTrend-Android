package com.buzzdynegamingteam.pricetrend.loginregister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginRegisterViewModel : ViewModel(){

    private val repo = LoginRegisterRepository

    private val _eventGoToHome = MutableLiveData<Boolean>()
    val eventGoToHome: LiveData<Boolean>
        get() = _eventGoToHome

    private val _eventGoToWelcome = MutableLiveData<Boolean>()
    val eventGoToWelcome: LiveData<Boolean>
        get() = _eventGoToWelcome

    init {
        _eventGoToHome.value = repo.isUserLoggedIn()
    }

    fun initCurrUserData() {
        viewModelScope.launch {
            repo.initCurrUserData()
        }
        _eventGoToWelcome.value = true
    }

    fun onAnyNavigateCompleted() {
        _eventGoToHome.value = false
        _eventGoToWelcome.value = false
    }
}