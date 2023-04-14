package org.devstrike.apps.android.unisach.storeside.features.auth.repositories

import org.devstrike.apps.android.unisach.storeside.network.Resource
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.*
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.RefreshTokenResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.SignUpResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.VerifyOtpResponse

/**
 * Created by Richard Uzor  on 11/02/2023
 */
interface AuthRepo {

    suspend fun createAccount(user: SignUpRequest): Resource<SignUpResponse>
    suspend fun verifyOtp(otp: VerifyOtpRequest): Resource<VerifyOtpResponse>
    suspend fun resendOtp(email: String): Resource<SignUpResponse>
    suspend fun googleSignIn(token: GoogleSignInRequest): Resource<VerifyOtpResponse>
    suspend fun login(userDetails: LoginRequest): Resource<VerifyOtpResponse>
    suspend fun refreshToken(): Resource<RefreshTokenResponse>
    suspend fun logout(): Resource<String>
    suspend fun forgotPassword(email: ForgotPasswordRequest): Resource<SignUpResponse>

}