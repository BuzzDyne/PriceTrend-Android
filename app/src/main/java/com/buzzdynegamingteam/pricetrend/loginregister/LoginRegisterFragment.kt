package com.buzzdynegamingteam.pricetrend.loginregister

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.databinding.LoginFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser

const val SIGN_IN_REQUEST_CODE = 200
const val TAG = "LoginRegisterFragment"

class LoginRegisterFragment : Fragment() {

    private val repo = LoginRegisterRepository
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<LoginFragmentBinding>(inflater,R.layout.login_fragment, container, false)

        viewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)


        bind.btnLogin.setOnClickListener{
            launchPreBuiltSignInFlow()
        }

        bind.btnGoogleSignin.setOnClickListener{
            // TODO Implement Custom-built login screen
        }

        viewModel.getCurrUser.observe(viewLifecycleOwner, Observer { currUser ->
            if(currUser != null){
                navigateToHomeScreen()
            }
        })

        return bind.root
    }


    private fun launchPreBuiltSignInFlow() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Toast.makeText(activity, "Welcome Back, ${repo.getCurrDisplayName()}!",Toast.LENGTH_SHORT).show()
                // TODO tell homeFragment if user is new somehow (Maybe check its user doc
                // TODO Maybe pass user doc to homeFragment to save read count
//                viewModel.initCurrUserData(repo.getCurrUser()!!.uid)
                viewModel.updateCurrUser()
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
                Toast.makeText(activity, "Sign in unsuccessful ${response?.error?.errorCode}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeScreen() {
        val action = LoginRegisterFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}