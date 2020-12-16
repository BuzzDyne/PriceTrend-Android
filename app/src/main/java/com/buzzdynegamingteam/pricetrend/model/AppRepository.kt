package com.buzzdynegamingteam.pricetrend.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AppRepository(application: Application) {
    private val app : Application = application
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var userMutableLiveData: MutableLiveData<FirebaseUser>


//    fun register(email: String, password: String ) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(app) {
//
//            }
//    }
}