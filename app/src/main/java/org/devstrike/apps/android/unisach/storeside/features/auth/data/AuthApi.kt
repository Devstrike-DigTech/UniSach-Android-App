/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.data

import com.bumptech.glide.load.engine.Resource
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests.*
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.RefreshTokenResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.SignUpResponse
import org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses.VerifyOtpResponse
import org.devstrike.apps.android.unisach.storeside.utils.Common.USER_BASE_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Richard Uzor  on 30/03/2023
 */
interface AuthApi {

    //MANUAL USER SIGN UP
    @Headers("Content-Type: application/json")
    @POST("$USER_BASE_URL/api/users/signup")
    suspend fun createAccount(
        @Body user: SignUpRequest
    ): SignUpResponse

    //MANUAL USER VERIFY OTP
    @Headers("Content-Type: application/json")
    @POST("$USER_BASE_URL/api/users/auth/verifyotp")
    suspend fun verifyOtp(
        @Body otp: VerifyOtpRequest
    ): VerifyOtpResponse

    //MANUAL USER RESEND OTP
    @Headers("Content-Type: application/json")
    @GET("$USER_BASE_URL/api/users/auth/resendotp/{email}")
    suspend fun resendOtp(
        @Path("email") email: String
    ): SignUpResponse

    //MANUAL USER GOOGLE SIGN IN
    @Headers("Content-Type: application/json")
    @POST("$USER_BASE_URL/api/users/auth/signin/google")
    suspend fun googleSignIn(
        @Body token: GoogleSignInRequest
    ): VerifyOtpResponse


    //MANUAL USER LOGIN
    @Headers("Content-Type: application/json")
    @POST("$USER_BASE_URL/api/users/auth/login")
    suspend fun login(
        @Body userDetails: LoginRequest
    ): VerifyOtpResponse


    //MANUAL USER REFRESH TOKEN
    @Headers("Content-Type: application/json")
    @GET("$USER_BASE_URL/api/users/auth/refreshtoken")
    suspend fun refreshToken(): RefreshTokenResponse

    //MANUAL USER LOGOUT
    @Headers("Content-Type: application/json")
    @GET("$USER_BASE_URL/api/users/auth/refreshtoken")
    suspend fun logout(): Nothing

    //MANUAL USER FORGOT PASSWORD
    @Headers("Content-Type: application/json")
    @POST("$USER_BASE_URL/api/users/auth/forgotpassword")
    suspend fun forgotPassword(
        @Body email: ForgotPasswordRequest
    ): SignUpResponse


}