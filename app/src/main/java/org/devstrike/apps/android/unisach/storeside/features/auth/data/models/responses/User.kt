/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 */

package org.devstrike.apps.android.unisach.storeside.features.auth.data.models.responses

data class User(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val email: String,
    val emailVerificationStatus: String,
    val first_name: String,
    val googleID: String? = "",
    val last_name: String,
    val name: String,
    val role: String
)