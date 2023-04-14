/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentHomeBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.auth.ui.AuthViewModel
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepoImpl
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class Home : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeRepoImpl>() {

    @set:Inject
    var authApi: AuthApi by Delegates.notNull()

    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getFragmentRepo() = HomeRepoImpl()

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

}