package com.buzzdynegamingteam.pricetrend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.buzzdynegamingteam.pricetrend.common.StringFormatter
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

        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.homeFragment,
                R.id.trackingListFragment,
                R.id.searchFragment,
                R.id.profileFragment,
                R.id.loginFragment
            )
            .build()

        NavigationUI.setupActionBarWithNavController(this, mController, appBarConfiguration)

        setupBottomNavMenu(mController)

        listenBackStackChange()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                when (intent.type) {
                    "text/plain" -> {
                        val t = intent.clipData?.getItemAt(0)?.text
                        val link = StringFormatter.extractLinkFromString(t.toString())
//                        Log.e("MainActivity", "onNewIntent: link $link")

                        val bundle = bundleOf("urlText" to link)
                        val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.homeFragment, true)
                                .build()

                        findNavController(R.id.myNavHostFragment).navigate(R.id.homeFragment, bundle, navOptions)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.setupWithNavController(navController)

        val noNavBarDestinations = listOf(
                R.id.loginFragment,
                R.id.trackingDetailFragment,
                R.id.searchDetailFragment,
                R.id.savingListFragment
                )

        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.loginFragment  -> bottomNav.visibility = View.GONE
//                else                -> bottomNav.visibility = View.VISIBLE
//            }
            if (destination.id in noNavBarDestinations) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
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