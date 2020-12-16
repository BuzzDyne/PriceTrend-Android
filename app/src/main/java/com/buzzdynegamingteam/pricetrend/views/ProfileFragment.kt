package com.buzzdynegamingteam.pricetrend.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.ProfileFragmentBinding


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<ProfileFragmentBinding>(inflater, R.layout.profile_fragment,
            container, false)

        // Inflate the layout for this fragment
        return bind.root
    }
}