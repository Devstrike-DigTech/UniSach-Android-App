/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.devstrike.apps.android.unisach.storeside.R
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentAuthScreenBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.GoogleSignInRequest
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.network.handleApiError
import org.devstrike.apps.android.unisach.storeside.utils.Common
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import org.devstrike.apps.android.unisach.storeside.utils.showProgressDialog
import org.devstrike.apps.android.unisach.storeside.utils.toast
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class AuthScreen : BaseFragment<AuthViewModel, FragmentAuthScreenBinding, AuthRepoImpl>() {

    @set:Inject
    var authApi: AuthApi by Delegates.notNull()

    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()

    val authViewModel: AuthViewModel by activityViewModels()

    private var progressDialog: Dialog? = null


    lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            subscribeToGoogleSignInEvent(idToken)

                            Log.d(Common.TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(Common.TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(Common.TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(Common.TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(Common.TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(
                                Common.TAG, "Couldn't get credential from result." +
                                        " (${e.localizedMessage})"
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val oneTapSignInButton: SignInButton = binding.authScreenOneTapSignInBtn

        with(binding){
            oneTapSignInButton.setOnClickListener {
                oneTapClient = Identity.getSignInClient(requireContext())
                signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            // Your server's client ID, not your Android client ID.
                            .setServerClientId(Common.CLIENT_ID)
                            // Only show accounts previously used to sign in.
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    // Automatically sign in when exactly one credential is retrieved.
                    .setAutoSelectEnabled(true)
                    .build()
                // ...
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity()) { result ->
                        try {
                            //oneTapSignInLauncher.launch(
                            startIntentSenderForResult(
                                result.pendingIntent.intentSender, REQ_ONE_TAP,
                                null, 0, 0, 0, null
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(Common.TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                        }
                    }
                    .addOnFailureListener(requireActivity()) { e ->
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(Common.TAG, e.localizedMessage)
                    }
            }
            authSignupBtn.setOnClickListener {
                val navToSignUp = AuthScreenDirections.actionAuthScreenToSignUp()
                findNavController().navigate(navToSignUp)
                findNavController().popBackStack(0, false)
            }
            authLoginBtn.setOnClickListener {
                val navToSignIn = AuthScreenDirections.actionAuthScreenToSignIn()
                findNavController().navigate(navToSignIn)
                findNavController().popBackStack(0, false)
            }
//                accountLogInBtnLogin.setOnClickListener {
//                    requireContext().toast("Login Success!!")
//                    val navToHome = SignInDirections.actionSignInToHome2()
//                    findNavController().navigate(navToHome)
//                }
//                accountLogInCreateAccount.setOnClickListener {
//                    val navToSignUp = SignInDirections.actionSignInToSignUp()
//                    findNavController().navigate(navToSignUp)
//                }
            }
        }

    private fun subscribeToGoogleSignInEvent(userToken: String?) {

        val token = GoogleSignInRequest(role = "Pharmacist", token = userToken!!)
        authViewModel.googleSignIn(token)

        lifecycleScope.launch {
            authViewModel.googleSignInState.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        hideProgressBar()
                        requireContext().toast("Sign in successful")
                        val navToHome = AuthScreenDirections.actionAuthScreenToHome2()
                        findNavController().navigate(navToHome)
                        findNavController().popBackStack(0, false)
                    }
                    is Resource.Failure -> {
                        hideProgressBar()
                        handleApiError(result.error) { subscribeToGoogleSignInEvent(userToken) }
                    }
                    is Resource.Loading -> {
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
    ) = FragmentAuthScreenBinding.inflate(inflater, container, false)

}