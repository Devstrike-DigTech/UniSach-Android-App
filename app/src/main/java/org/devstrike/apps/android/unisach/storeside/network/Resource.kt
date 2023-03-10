/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.network

import okhttp3.ResponseBody

/**
 * sealed class that defines the states of a network call and how to handle their received data
 * it is called and utilized everywhere that a network call is made
 * Created by Richard Uzor  on 26/12/2022
 */
sealed class Resource<out T>(
    val value: T? = null,
    val error: Throwable? = null
) {
    class Success<out T>(value: T, errorMessage: Throwable? = null) : Resource<T>(value, errorMessage)

    class Failure<T>(
        throwable: Throwable? = null, value: T? = null
    ) : Resource<T>(value, throwable)

    object Loading : Resource<Nothing>()
}
