package com.buzzdynegamingteam.pricetrend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.buzzdynegamingteam.pricetrend.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    private lateinit var bind: ActivityMainBinding
    private lateinit var mSharedViewModel: SharedViewModel
    private lateinit var mController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        toolbar = supportActionBar!!

        //https://stackoverflow.com/questions/53846870/how-to-use-viewmodelproviders-in-kotlin
        mSharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        mController = Navigation.findNavController(this, R.id.myNavHostFragment)

        setupBottomNavMenu(mController)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment  -> bottomNav.visibility = View.GONE
                else                -> bottomNav.visibility = View.VISIBLE
            }
        }

    }

    fun checkUserLogin(auth: FirebaseAuth) {
        var currUser = auth.currentUser
        if (currUser != null) {

        }
    }



}