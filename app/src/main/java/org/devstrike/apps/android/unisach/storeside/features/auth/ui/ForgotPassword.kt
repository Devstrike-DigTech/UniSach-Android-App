/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.devstrike.apps.android.unisach.storeside.R
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentForgotPasswordBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.ForgotPasswordRequest
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.network.handleApiError
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import org.devstrike.apps.android.unisach.storeside.utils.enable
import org.devstrike.apps.android.unisach.storeside.utils.showProgressDialog
import org.devstrike.apps.android.unisach.storeside.utils.snackbar
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class ForgotPassword : BaseFragment<AuthViewModel, FragmentForgotPasswordBinding, AuthRepoImpl>() {

    @set:Inject
    var authApi: AuthApi by Delegates.notNull()

    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()

    val authViewModel: AuthViewModel by activityViewModels()

    lateinit var email: String

    private var progressDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            accountForgotPasswordBtn.enable(false)

            forgotPasswordEmail.addTextChangedListener {
                email = it.toString().trim()
                accountForgotPasswordBtn.enable(email.isNotEmpty())
                accountForgotPasswordBtn.setOnClickListener {
                    subscribeToForgotPasswordEvent(email)
                }
            }
        }
    }

    private fun subscribeToForgotPasswordEvent(email: String) {
        val emailBody = ForgotPasswordRequest(
            email = email
        )
        authViewModel.forgotPassword(emailBody)
        lifecycleScope.launch {
            authViewModel.forgotPasswordState.collect{result ->
                when(result){
                    is Resource.Success ->{
                        hideProgressBar()
                        requireView().snackbar(result.value!!.data)
                    }
                    is Resource.Failure ->{
                        hideProgressBar()
                        handleApiError(result.error){subscribeToForgotPasswordEvent(email)}
                    }
                    is Resource.Loading ->{
                        showProgressBar()
                    }
                }

            }
        }
    }

    private fun showProgressBar() {
        hideProgressBar()
        progressDialog = requireActivity().showProgressDialog()
    }

    private fun hideProgressBar() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }


    override fun getFragmentRepo() = AuthRepoImpl(authApi, sessionManager)

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForgotPasswordBinding.inflate(inflater, container, false)


}