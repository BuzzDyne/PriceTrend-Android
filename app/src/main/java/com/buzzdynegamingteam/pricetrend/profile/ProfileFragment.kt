package com.buzzdynegamingteam.pricetrend.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.ProfileFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<ProfileFragmentBinding>(inflater, R.layout.profile_fragment,
            container, false)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        viewModel.getEventNavigateToLogin.observe(viewLifecycleOwner, Observer { isLoggedOut ->
            if (isLoggedOut) {
                navigateToLogin()
            }
        })

        bind.textUsername.text = FirebaseAuth.getInstance().currentUser!!.displayName

        bind.btnLogout.setOnClickListener {
            viewModel.signOut()
        }

        return bind.root
    }

    private fun navigateToLogin() {
        AuthUI.getInstance().signOut(requireContext())
        val action = ProfileFragmentDirections.actionProfileFragmentToHomeFragment()
        findNavController().navigate(action)
        viewModel.onNavigateToLoginFinished()
    }
}