/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.devstrike.apps.android.unisach.storeside.utils.Common.JWT_TOKEN_KEY
import org.devstrike.apps.android.unisach.storeside.utils.Common.USER_EMAIL_KEY
import org.devstrike.apps.android.unisach.storeside.utils.Common.USER_FIRST_NAME_KEY
import org.devstrike.apps.android.unisach.storeside.utils.Common.USER_ID_KEY
import org.devstrike.apps.android.unisach.storeside.utils.Common.USER_NAME_KEY

/**
 * class to handle session management of the user including things like token management
 * Created by Richard Uzor  on 23/12/2022
 */
class SessionManager(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session_manager")

    suspend fun updateSession(token: String, firstName: String, name: String, email: String, id: String) {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val userFirstName = stringPreferencesKey(USER_FIRST_NAME_KEY)
        val userName = stringPreferencesKey(USER_NAME_KEY)
        val userEmail = stringPreferencesKey(USER_EMAIL_KEY)
        val userId = stringPreferencesKey(USER_ID_KEY)
        context.dataStore.edit { preferences ->
            preferences[jwtTokenKey] = token
            preferences[userName] = name
            preferences[userFirstName] = firstName
            preferences[userEmail] = email
            preferences[userId] = id
        }
    }

    suspend fun getJwtToken(): String? {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[jwtTokenKey]
    }

    suspend fun getCurrentUserFirstName(): String? {
        val userFirstNameKey = stringPreferencesKey(USER_FIRST_NAME_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userFirstNameKey]
    }

    suspend fun getCurrentUserName(): String? {
        val userNameKey = stringPreferencesKey(USER_NAME_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userNameKey]
    }

    suspend fun getCurrentUserEmail(): String? {
        val userEmailKey = stringPreferencesKey(USER_EMAIL_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userEmailKey]
    }

    suspend fun getCurrentUserId(): String? {
        val userIdKey = stringPreferencesKey(USER_ID_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userIdKey]
    }

    suspend fun logout() {
        context.dataStore.edit {
            it.clear()
        }
    }
}