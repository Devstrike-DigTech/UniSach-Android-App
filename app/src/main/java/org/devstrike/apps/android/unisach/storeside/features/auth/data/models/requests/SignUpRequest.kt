/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.data.models.requests

data class SignUpRequest(
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val phone: String,
    val role: String
)