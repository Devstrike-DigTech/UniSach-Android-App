package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.devstrike.apps.android.unisach.storeside.R
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentSignUpBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl

@AndroidEntryPoint
class SignUp : BaseFragment<AuthViewModel, FragmentSignUpBinding, AuthRepoImpl>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            accountCreateAccountLogIn.setOnClickListener {
                val navToSignIn = SignUpDirections.actionSignUpToSignIn()
                findNavController().navigate(navToSignIn)
            }
            accountSignupBtnSignup.setOnClickListener {
                val navToRegisterPharmacy = SignUpDirections.actionSignUpToPharmacyProfileReg()
                findNavController().navigate(navToRegisterPharmacy)
            }
        }

    }

    override fun getFragmentRepo() = AuthRepoImpl()

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

}