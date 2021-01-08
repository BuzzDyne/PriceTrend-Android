package com.buzzdynegamingteam.pricetrend.loginregister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginRegisterViewModel : ViewModel(){

    private val repo = CommonRepository

    private val _currUser = MutableLiveData<FirebaseUser?>()
    val getCurrUser: LiveData<FirebaseUser?>
        get() = _currUser

    private val _loginFinished = MutableLiveData<Boolean>()
    val _getLoginFinished: LiveData<Boolean>
        get() = _loginFinished

    init {
        _loginFinished.value = false
        updateCurrUser()
    }

    private fun initCurrUserData() {
        val uname = _currUser.value!!.displayName?: "Guest"
        val uid = _currUser.value!!.uid

        viewModelScope.launch {
            repo.initCurrUserData(uid, uname)
            _loginFinished.value = true
        }
    }

    fun updateCurrUser() {
        _currUser.value = repo.getCurrUser()
        if(_currUser.value != null) {
            initCurrUserData()
        }
    }

    fun getDisplayName(): String {
        return repo.getCurrDisplayName()!!
    }
}