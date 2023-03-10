package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import org.devstrike.apps.android.unisach.storeside.base.BaseViewModel
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 11/02/2023
 */
/**
 * Created by Richard Uzor  on 11/02/2023
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepoImpl,
): BaseViewModel(authRepo) {

}