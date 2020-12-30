package com.buzzdynegamingteam.pricetrend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buzzdynegamingteam.pricetrend.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    private lateinit var bind: ActivityMainBinding
    private lateinit var mController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        toolbar = supportActionBar!!

        mController = Navigation.findNavController(this, R.id.myNavHostFragment)

        setupBottomNavMenu(mController)

        listenBackStackChange()
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

    private fun listenBackStackChange() {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.myNavHostFragment)

        // ChildFragmentManager of NavHostFragment
        val navHostChildFragmentManager = navHostFragment?.childFragmentManager

        navHostChildFragmentManager?.addOnBackStackChangedListener {

            val backStackEntryCount = navHostChildFragmentManager.backStackEntryCount
            val fragments = navHostChildFragmentManager.fragments

            Log.e("TAG", "😛 NavHost count: $backStackEntryCount, fragments: $fragments")
        }
    }

}