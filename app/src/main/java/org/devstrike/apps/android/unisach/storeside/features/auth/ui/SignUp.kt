package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chaos.view.PinView
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.devstrike.apps.android.unisach.storeside.R
import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.base.BaseFragment
import org.devstrike.apps.android.unisach.storeside.databinding.FragmentSignUpBinding
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.GoogleSignInRequest
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.SignUpRequest
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.VerifyOtpRequest
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.network.handleApiError
import org.devstrike.apps.android.unisach.storeside.utils.*
import org.devstrike.apps.android.unisach.storeside.utils.Common.CLIENT_ID
import org.devstrike.apps.android.unisach.storeside.utils.Common.COUNTDOWN_IN_MILLIS
import org.devstrike.apps.android.unisach.storeside.utils.Common.TAG
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SignUp : BaseFragment<AuthViewModel, FragmentSignUpBinding, AuthRepoImpl>() {

    @set:Inject
    var authApi: AuthApi by Delegates.notNull()

    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()

    private var progressDialog: Dialog? = null

    val authViewModel: AuthViewModel by activityViewModels()

    lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true


//    var oneTapSignInLauncher: ActivityResultLauncher<IntentSenderRequest> =
//        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
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

                        Log.d(TAG, "Got ID token.")
                    }
                    password != null -> {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d(TAG, "Got password.")
                    }
                    else -> {
                        // Shouldn't happen.
                        Log.d(TAG, "No ID token or password!")
                    }
                }
            } catch (e: ApiException) {
                // ...
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d(TAG, "One-tap dialog was closed.")
                        // Don't re-prompt the user.
                        showOneTapUI = false
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d(TAG, "One-tap encountered a network error.")
                        // Try again or just ignore.
                    }
                    else -> {
                        Log.d(
                            TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})"
                        )
                    }
                }
            }
        }
    }
}

//                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)//result.data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
//                // Use the credential to sign in the user
//                val userToken = credential.googleIdToken//credential?.idTokens
//                subscribeToGoogleSignInEvent( userToken)
//                // ...
//            } else {
//                // The user did not select any credential, or cancelled the flow
//                // ...
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            val oneTapSignInButton: SignInButton = oneTapSignInBtn
//
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
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d(TAG, e.localizedMessage)
                }

//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//            oneTapClient = Identity.getSignInClient(requireContext())
//
//
//            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
//            val oneTapSignInButton: SignInButton = oneTapSignInBtn
//
//            oneTapSignInButton.setOnClickListener {
//                val hintRequest = HintRequest.Builder()
//                    .setEmailAddressIdentifierSupported(true)
//                    .build()
//
//                val credentialsClient = Credentials.getClient(requireContext())
//
//                val intent = credentialsClient.getHintPickerIntent(hintRequest)
//                oneTapSignInLauncher.launch(
//                    IntentSenderRequest.Builder(intent.intentSender).build()
//                )
        }


        accountCreateAccountLogIn.setOnClickListener {
            val navToSignIn = SignUpDirections.actionSignUpToSignIn()
            findNavController().navigate(navToSignIn)
        }
        accountSignupBtnSignup.setOnClickListener {
            subscribeToSignUpEvent()
        }
    }
        }


    private fun subscribeToSignUpEvent() {

        val email = binding.signUpEmail.text.toString().trim()
        val firstName = binding.signUpFirstName.toString().trim()
        val lastName = binding.signUpLastName.toString().trim()
        val password = binding.signUpPassword.toString().trim()
        val confirmPassword = binding.signUpConfirmPassword.toString().trim()
        val phone = binding.signUpPhoneNumber.toString().trim()
        val role = "Pharmacist"

        val signUpRequest = SignUpRequest(
            email = email,
            first_name = firstName,
            last_name = lastName,
            password = password,
            phone = phone,
            role = role
        )

        authViewModel.createAccount(signUpRequest, confirmPassword)

        lifecycleScope.launch {
            authViewModel.registerState.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        hideProgressBar()
                        // TODO: launch otp bottom sheet
                        launchBottomSheet(email)
                        requireContext().toast("Sign up success")
                        val navToRegisterPharmacy =
                            SignUpDirections.actionSignUpToPharmacyProfileReg()
                        findNavController().navigate(navToRegisterPharmacy)
                    }
                    is Resource.Failure -> {
                        hideProgressBar()
                        handleApiError(result.error)
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }

            }
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
                        // TODO: launch otp bottom sheet
                        requireContext().toast("Sign in successful")
                        val navToPharmReg = SignUpDirections.actionSignUpToPharmacyProfileReg()
                        findNavController().navigate(navToPharmReg)
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


    private fun launchBottomSheet(email: String) {
        //show the otp page as a bottom sheet
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.reg_otp_layout, null)

        val otpView = view.findViewById<PinView>(R.id.otp_view)
        val otpCountDownView = view.findViewById<TextView>(R.id.sign_up_otp_count_down)
        val otpVerifyBtn = view.findViewById<MaterialButton>(R.id.otp_verify_btn)
        otpCountDownView.setTextColor(resources.getColor(R.color.custom_color))

        otpCountDownView.enable(false)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()

        var timeLeftInMillis = COUNTDOWN_IN_MILLIS

        val timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (millisUntilFinished / 1000) / 60

                val seconds = (millisUntilFinished / 1000) % 60

                //display timer in minutes and seconds
                val timeFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                otpCountDownView.text = timeFormatted
            }

            override fun onFinish() {
                //enable view to request for new otp after 3 minutes
                timeLeftInMillis = 0
                otpCountDownView.enable(true)
            }
        }
        timer.start()

        otpVerifyBtn.setOnClickListener {
            val enteredOtp = otpView.text.toString().trim()
            timer.cancel()
            dialog.dismiss()
            verifyOtp(email, enteredOtp)

        }

//        otpView!!.setOtpCompletionListener(object : OnOtpCompletionListener {
//            override fun onOtpCompleted(otp: String?) {
//                Log.d(TAG, "onOtpCompleted: ${otp.toString()}")
//


//
//            }
//
//        })

        otpCountDownView.setOnClickListener {
            //enable request another otp when timer elapses
            resendEmailForOtp(email)
        }


    }

    private fun resendEmailForOtp(personalEmail: String) {
        authViewModel.resendOtp(personalEmail)
        lifecycleScope.launch {
            authViewModel.resendOtpState.collect{result ->
                when(result){
                    is Resource.Success ->{
                        hideProgressBar()
                        launchBottomSheet(personalEmail)
                    }
                    is Resource.Failure ->{
                        hideProgressBar()
                        handleApiError(result.error){resendEmailForOtp(personalEmail)}
                    }
                    is Resource.Loading ->{
                        showProgressBar()
                    }
                }

            }
        }
    }

    private fun verifyOtp(email: String, enteredOtp: String) {
        val verifyOtpRequest = VerifyOtpRequest(
            email = email,
            otp = enteredOtp
        )
        authViewModel.verifyOtp(verifyOtpRequest)
        lifecycleScope.launch {
            authViewModel.verifyOtpState.collect{ result ->
                when(result){
                    is Resource.Success ->{
                        hideProgressBar()
                        val navToPharmReg = SignUpDirections.actionSignUpToPharmacyProfileReg()
                        findNavController().navigate(navToPharmReg)
                    }
                    is Resource.Failure ->{
                        hideProgressBar()
                        handleApiError(result.error){verifyOtp(email, enteredOtp)}
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
    ) = FragmentSignUpBinding.inflate(inflater, container, false)


}