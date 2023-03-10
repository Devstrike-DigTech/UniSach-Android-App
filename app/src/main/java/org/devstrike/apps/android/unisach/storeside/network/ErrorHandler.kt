/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.network

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * this file handles all API errors
 * Created by Richard Uzor  on 26/12/2022
 */


val TAG = "ErrorHandler"

//function to present an error message in a snack bar with the retry option
fun View.retrySnackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("RETRY") {
            it()
        }
    }
    snackbar.show()
}

//function to present an error message in a snack bar with the undo option
fun View.undoSnackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("UNDO") {
            it()
        }
    }
    snackbar.show()
}

//function to define an api error failure state
fun Fragment.handleApiError(failure: Throwable?, retry: (() -> Unit)? = null) {
//    when {
//        failure.isNetworkError == true -> requireView().snackbar(
//            "Please check your internet connection",
//            retry
//        )
//        failure.errorCode == 400 -> {
//            if (this is SignIn) {
//                requireView().snackbar("Incorrect email or password")
//            } else {
////                requireView().snackbar("Will do logout")
//                (this as BaseFragment<*, *, *>).logout()
//            }
//        }
//        else -> {
    val error = failure?.localizedMessage.toString()
    requireView().retrySnackbar(error, retry)
    Log.d(TAG, "handleApiError: $error")
}
//    }
//}