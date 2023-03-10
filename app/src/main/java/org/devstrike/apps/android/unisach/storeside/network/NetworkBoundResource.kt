/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.network

import kotlinx.coroutines.flow.*

/**
 * this file manages network network bound data fetching
 * when implemented, it checks the database content and only fetches from the cloud if a news item is available
 * Created by Richard Uzor  on 26/12/2022
 */


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>, //responsible for querying the room db
    crossinline fetch: suspend () -> RequestType, //responsible for querying the cloud db
    crossinline saveFetchResult: suspend (RequestType) -> Unit, //responsible for inserting result into room db
    crossinline shouldFetch: (ResultType) -> Boolean = { true } //responsible for checking if data in room db is stale
) = flow {
    val data = query().first() //fetches the first instance of the room db list

    //the result of the resource check is assigned to a variable which is then emitted at the end
    val flow = if (shouldFetch(data)) { //if the data is stale
        emit(Resource.Loading) //loading state
        try {
            //query the cloud db
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Failure(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}