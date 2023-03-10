/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.home.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import org.devstrike.apps.android.unisach.storeside.base.BaseViewModel
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepoImpl
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 17/02/2023
 */
/**
 * Created by Richard Uzor  on 17/02/2023
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepoImpl
) :BaseViewModel(homeRepo) {
}