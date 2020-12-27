package com.buzzdynegamingteam.pricetrend.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.ProfileFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    val TAG = "ProfileFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<ProfileFragmentBinding>(inflater, R.layout.profile_fragment,
            container, false)

        bind.textUsername.text = FirebaseAuth.getInstance().currentUser!!.displayName

        bind.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            AuthUI.getInstance().signOut(requireContext())

//            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
//            findNavController().navigate(action)
        }

        // Inflate the layout for this fragment
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: ")
    }
}