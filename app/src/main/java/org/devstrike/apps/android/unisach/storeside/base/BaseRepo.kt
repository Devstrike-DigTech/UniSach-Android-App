/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.devstrike.apps.android.unisach.storeside.network.Resource
import retrofit2.HttpException

/**
 * As required by the MVVM architecture, every fragment or activity should have a repository class
 * Thus, there will be need to initialize the repo at every fragment.
 * Therefore this class is written to avoid repetition
 * In this class, we write the function to safely make api calls abd handle the errors
 * Created by Richard Uzor  on 26/12/2022
 */
open class BaseRepo {

    //suspend function to make api call safely
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            //run api call in IO thread
            try {
                //if call returns successful response, set its values to the resource sealed class
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                //if call returns unsuccessful response...
                when (throwable) {
                    is HttpException -> {
                        //if the error response is a http error set its response to the corresponding parameters in the resource sealed class
                        Resource.Failure(throwable)
                    }
                    else -> {
                        //if the error response is a network error set its response to the corresponding parameters in the resource sealed class
                        Resource.Failure(throwable)
                    }
                }
            }
        }
    }

//    suspend fun logout(api: AuthApi) = safeApiCall {
//        api.logout()
//    }


}