package com.buzzdynegamingteam.pricetrend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.LoginFragmentBinding


class LoginRegisterFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val bind = DataBindingUtil.inflate<LoginFragmentBinding>(inflater,R.layout.login_fragment,
            container, false)


//        bind.btnLogin.setOnClickListener(
//            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_homeFragment)
//        )
        bind.btnLogin.setOnClickListener{
            val action = LoginRegisterFragmentDirections.actionLoginFragmentToHomeFragment(true)
            findNavController().navigate(action)
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(DeleteViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//    }

}