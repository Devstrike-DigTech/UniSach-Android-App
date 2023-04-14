package org.devstrike.apps.android.unisach.storeside.features.auth.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.base.BaseRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.*
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.RefreshTokenResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.SignUpResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.VerifyOtpResponse
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 11/02/2023
 */
/**
 * Created by Richard Uzor  on 11/02/2023
 */
@ActivityRetainedScoped
class AuthRepoImpl @Inject constructor(
    val authApi: AuthApi,
    val sessionManager: SessionManager
) : AuthRepo, BaseRepo() {
    override suspend fun createAccount(user: SignUpRequest): Resource<SignUpResponse> {

        return safeApiCall{
            //create user using api
            // TODO: check network connection
            authApi.createAccount(user)
        }

    }

    override suspend fun verifyOtp(otp: VerifyOtpRequest): Resource<VerifyOtpResponse> {
        return safeApiCall {
            authApi.verifyOtp(otp)
        }
    }

    override suspend fun resendOtp(email: String): Resource<SignUpResponse> {
        return safeApiCall {
            authApi.resendOtp(email)
        }
    }

    override suspend fun googleSignIn(token: GoogleSignInRequest): Resource<VerifyOtpResponse> {
        return safeApiCall {
            authApi.googleSignIn(token)
        }
    }

    override suspend fun login(userDetails: LoginRequest): Resource<VerifyOtpResponse> {
        return safeApiCall {
            authApi.login(userDetails)
        }
    }

    override suspend fun refreshToken(): Resource<RefreshTokenResponse> {
        return safeApiCall {
            authApi.refreshToken()
        }
    }

    override suspend fun logout(): Resource<String> {
        return try {
            //authApi.logout()
            sessionManager.logout()
            Resource.Success("Logged out")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(value = e.message ?: "Some problem occurred")
        }
    }

    override suspend fun forgotPassword(email: ForgotPasswordRequest): Resource<SignUpResponse> {
        return safeApiCall { authApi.forgotPassword(email) }
    }
}