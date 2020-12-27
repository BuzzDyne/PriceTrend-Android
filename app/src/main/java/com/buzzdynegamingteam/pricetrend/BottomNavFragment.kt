package com.buzzdynegamingteam.pricetrend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.buzzdynegamingteam.pricetrend.databinding.BottomNavFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {

    private lateinit var mController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<BottomNavFragmentBinding>(inflater, R.layout.bottom_nav_fragment,
            container, false)

        //Setup BottomNav
        mController = findNavController()

        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_nav_host) as NavHostFragment
//        navHostFragment.navController.navigate(R.id.profileFragment)

//        bind.bottomNavbar.setupWithNavController(navHostFragment.navController)
        NavigationUI.setupWithNavController(bind.bottomNavbar as BottomNavigationView, navHostFragment.navController)

//        val bottomNavBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navbar)
//        val bottomNavBar = bind.bottomNavbar
//        bottomNavBar!!.setupWithNavController(mController)

//        bind.bottomNavbar.setupWithNavController(mController)

        return bind.root
    }

}