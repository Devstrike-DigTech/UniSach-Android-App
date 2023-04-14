package org.devstrike.apps.android.unisach.storeside.features.auth.ui

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.base.BaseViewModel
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.*
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.RefreshTokenResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.SignUpResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.VerifyOtpResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.utils.Common.MAXIMUM_PASSWORD_LENGTH
import org.devstrike.apps.android.unisach.storeside.utils.Common.MINIMUM_PASSWORD_LENGTH
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
) : BaseViewModel(authRepo) {


    // = = = = = = = = = = = = = = = = = = = = STATES = = = = = = = = = = = = = = = = = = = = = = =
    //state for register user
    private val _registerState = MutableSharedFlow<Resource<SignUpResponse>>()
    val registerState: SharedFlow<Resource<SignUpResponse>> = _registerState

    //state for user verifying otp
    private val _verifyOtpState = MutableSharedFlow<Resource<VerifyOtpResponse>>()
    val verifyOtpState: SharedFlow<Resource<VerifyOtpResponse>> = _verifyOtpState

    //state for resending otp
    private val _resendOtpState = MutableSharedFlow<Resource<SignUpResponse>>()
    val resendOtpState: SharedFlow<Resource<SignUpResponse>> = _resendOtpState

    //state for google sign in
    private val _googleSignInState = MutableSharedFlow<Resource<VerifyOtpResponse>>()
    val googleSignInState: SharedFlow<Resource<VerifyOtpResponse>> = _googleSignInState

    //state for user log in
    private val _logInState = MutableSharedFlow<Resource<VerifyOtpResponse>>()
    val logInState: SharedFlow<Resource<VerifyOtpResponse>> = _logInState

    //state for refresh token
    private val _refreshTokenState = MutableSharedFlow<Resource<RefreshTokenResponse>>()
    val refreshTokenState: SharedFlow<Resource<RefreshTokenResponse>> = _refreshTokenState

    //state for forgot password
    private val _forgotPasswordState = MutableSharedFlow<Resource<SignUpResponse>>()
    val forgotPasswordState: SharedFlow<Resource<SignUpResponse>> = _forgotPasswordState


    //= = = = = = = = = = = = = = = = = = = = = FUNCTIONS = = = = = = = = = = = = = = = = = = = = =

    fun createAccount(user: SignUpRequest, confirmPassword: String) = viewModelScope.launch {
        _registerState.emit(Resource.Loading)

        // input validations
        //input validations
        if (user.email.isEmpty() || user.first_name.isEmpty() || user.last_name.isEmpty() || user.phone.isEmpty() || user.password.isEmpty() || user.role.isEmpty() || confirmPassword.isEmpty()) {
            _registerState.emit(Resource.Failure(throwable = Throwable(message = "Some Fields are Empty")))
            return@launch
        }

        //email validity check
        if (!isEmailValid(user.email)) {
            _registerState.emit(Resource.Failure(throwable = Throwable(message = "Email is not Valid")))
            return@launch
        }

        //password validity check
        if (!isPasswordValid(user.password)) {
            _registerState.emit(Resource.Failure(throwable = Throwable(message = "Password length should be between $MINIMUM_PASSWORD_LENGTH and $MAXIMUM_PASSWORD_LENGTH")))
            return@launch
        }

        //password double check
        if (user.password != confirmPassword) {
            _registerState.emit(Resource.Failure(throwable = Throwable(message = "Password does not match")))
            return@launch
        }

        //use the state to call the create user api
        _registerState.emit(authRepo.createAccount(user))

    }

    fun verifyOtp(otp: VerifyOtpRequest) = viewModelScope.launch {
        _verifyOtpState.emit(Resource.Loading)
        _verifyOtpState.emit(authRepo.verifyOtp(otp))
    }

    fun resendOtp(email: String) = viewModelScope.launch {
        _resendOtpState.emit(Resource.Loading)
        _resendOtpState.emit(authRepo.resendOtp(email))
    }

    fun googleSignIn(token: GoogleSignInRequest) = viewModelScope.launch {
        _googleSignInState.emit(Resource.Loading)
        _googleSignInState.emit(authRepo.googleSignIn(token))
    }

    fun login(userDetails: LoginRequest) = viewModelScope.launch {
        _logInState.emit(Resource.Loading)
        _logInState.emit(authRepo.login(userDetails))
    }

    fun refreshToken() = viewModelScope.launch {
        _refreshTokenState.emit(Resource.Loading)
        _refreshTokenState.emit(authRepo.refreshToken())
    }

    fun logout() = viewModelScope.launch {
        authRepo.logout()
    }

    fun forgotPassword(email: ForgotPasswordRequest) = viewModelScope.launch {
        _forgotPasswordState.emit(Resource.Loading)
        _forgotPasswordState.emit(authRepo.forgotPassword(email))
    }



    // = = = = = = = = = = = = = = = = = = = = VALIDATIONS = = = = = = = = = = = = = = = = = = = =
    //email validation function
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //password validation function
    private fun isPasswordValid(password: String): Boolean {
        return (password.length in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH)
    }

}