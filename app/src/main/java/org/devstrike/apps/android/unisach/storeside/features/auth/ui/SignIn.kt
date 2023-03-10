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
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentSignInBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.utils.toast

@AndroidEntryPoint
class SignIn : BaseFragment<AuthViewModel, FragmentSignInBinding, AuthRepoImpl>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            accountLogInBtnLogin.setOnClickListener {
                requireContext().toast("Login Success!!")
                val navToHome = SignInDirections.actionSignInToHome2()
                findNavController().navigate(navToHome)
            }
            accountLogInCreateAccount.setOnClickListener {
                val navToSignUp = SignInDirections.actionSignInToSignUp()
                findNavController().navigate(navToSignUp)
            }
        }
    }


    override fun getFragmentRepo() = AuthRepoImpl()

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

}