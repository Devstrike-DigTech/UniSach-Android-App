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
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentPharmacyAddressRegBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.auth.ui.AuthViewModel

@AndroidEntryPoint
class PharmacyAddressReg : BaseFragment<AuthViewModel, FragmentPharmacyAddressRegBinding, AuthRepoImpl>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnAddressBack.setOnClickListener {
                val navBackToProfile = PharmacyAddressRegDirections.actionPharmacyAddressRegToPharmacyProfileReg()
                findNavController().navigate(navBackToProfile)
            }

            btnAddressNext.setOnClickListener {
                val navToOthersReg = PharmacyAddressRegDirections.actionPharmacyAddressRegToPharmacyOthersReg()
                findNavController().navigate(navToOthersReg)
            }
        }

    }
    override fun getFragmentRepo() = AuthRepoImpl()

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPharmacyAddressRegBinding.inflate(inflater, container, false)

}