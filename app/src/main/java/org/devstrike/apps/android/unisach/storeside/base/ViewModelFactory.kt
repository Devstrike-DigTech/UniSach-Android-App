/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.auth.ui.AuthViewModel
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.home.ui.HomeViewModel

/**
 * The viewModel factory class is a base class to create a viewModel and provide it to a fragment only if it is found
 * At this base point, it assigns the repo of each of the fragments to the viewModels
 * Created by Richard Uzor  on 26/12/2022
 */
class ViewModelFactory(
    private val repo: BaseRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            //... it checks the viewModel which is using it and casts the base repo to that viewModel's repo
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repo as AuthRepoImpl) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repo as HomeRepoImpl) as T


            else -> throw IllegalAccessException("ViewModelClass Not Found")
        }
    }
}