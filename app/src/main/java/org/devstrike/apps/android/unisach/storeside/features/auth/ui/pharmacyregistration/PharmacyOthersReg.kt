/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.ui.pharmacyregistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.devstrike.apps.android.unisach.storeside.R
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentPharmacyOthersRegBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.auth.ui.AuthViewModel
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class PharmacyOthersReg : BaseFragment<AuthViewModel, FragmentPharmacyOthersRegBinding, AuthRepoImpl>() {
    @set:Inject
    var authApi: AuthApi by Delegates.notNull()

    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnOthersBack.setOnClickListener {
                val navBackToAddressReg = PharmacyOthersRegDirections.actionPharmacyOthersRegToPharmacyAddressReg()
                findNavController().navigate(navBackToAddressReg)
            }
            btnOthersFinish.setOnClickListener {
                val navToHome = PharmacyOthersRegDirections.actionPharmacyOthersRegToHome2()
                findNavController().navigate(navToHome)
            }
        }

    }

    override fun getFragmentRepo() = AuthRepoImpl(authApi, sessionManager)

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPharmacyOthersRegBinding.inflate(inflater, container, false)

}