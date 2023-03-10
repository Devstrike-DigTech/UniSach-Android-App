/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.utils

/**
 * This is an object (accessible everywhere in the project) that contains all variables that could be needed anywhere in the project
 * Created by Richard Uzor  on 15/12/2022
 */
object Common {

    var userId = ""

    //base url for all api calls
    const val BASE_URL = "https://fair-cyan-crayfish-sock.cyclic.app/"
    const val USER_BASE_URL = "https://citrab.onrender.com"


    const val JWT_TOKEN_KEY = "jwt_token_key"
    const val USER_NAME_KEY = "user_name"
    const val USER_EMAIL_KEY = "user_email"
    const val USER_ID_KEY = "user_id"
    const val MINIMUM_PASSWORD_LENGTH = 4
    const val MAXIMUM_PASSWORD_LENGTH = 8


    //the index request page number for pagination fetching
    const val NEWS_LIST_INDEX_PAGE = 1

    //the size of items for each page of the news list
    const val NETWORK_PAGE_SIZE = 25

    //the name of our app database
    const val LOCAL_DB_NAME = "citrarb_db"
    const val TAG = "EQUA"

    //variable to store a link shared from an outside source to open in the app
    var deepLinkNewsUrl = ""


    data class CalendarDetails(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int,
        val dayOfWeek: Int,
        val hour: Int,
        val minute: Int,
        val second: Int
    )
}