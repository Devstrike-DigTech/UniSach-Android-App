/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)