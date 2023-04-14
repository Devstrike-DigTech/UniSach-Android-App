package org.devstrike.apps.android.unisach.storeside.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.devstrike.apps.android.unisach.storeside.base.BaseRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.data.AuthApi
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepo
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepoImpl
import org.devstrike.apps.android.unisach.storeside.utils.Common.BASE_URL
import org.devstrike.apps.android.unisach.storeside.utils.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Richard Uzor  on 11/02/2023
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //Provide Gson
    @Singleton
    @Provides
    fun provideGson() = Gson()

    //Initialize, Build and Provide Retrofit Instance
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    //Provide Session Manager
    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManager(context)

    // = = = = = = = = = = = = = = = = = = = PROVIDE APIS = = = = = = = = = = = = = = = = = = = = =

    //Provides the Authentication api passing the built retrofit instance
    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)


    // = = = = = = = = = = = = = = = = = = PROVIDE REPOS = = = = = = = = = = = = = = = = = = = = = =

    @Singleton
    @Provides
    fun providesBaseRepo() = BaseRepo()

    //provide auth repo returning the implementation parameters
    @Singleton
    @Provides
    fun providesAuthRepo(
        authApi: AuthApi,
        sessionManager: SessionManager
    ): AuthRepo {
        return AuthRepoImpl(
            authApi, sessionManager
        )
    }

    //provide home (landing) repo returning the implementation parameters
    @Singleton
    @Provides
    fun providesHomeRepo(
    ): HomeRepo {
        return HomeRepoImpl()
    }

}