package org.devstrike.apps.android.unisach.storeside.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.devstrike.apps.android.unisach.storeside.base.BaseRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepo
import org.devstrike.apps.android.unisach.storeside.features.auth.repositories.AuthRepoImpl
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepo
import org.devstrike.apps.android.unisach.storeside.features.home.repositories.HomeRepoImpl
import javax.inject.Singleton

/**
 * Created by Richard Uzor  on 11/02/2023
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providesBaseRepo() = BaseRepo()


    //provide auth repo returning the implementation parameters
    @Singleton
    @Provides
    fun providesAuthRepo(
    ): AuthRepo {
        return AuthRepoImpl()
    }

    //provide home (landing) repo returning the implementation parameters
    @Singleton
    @Provides
    fun providesHomeRepo(
    ): HomeRepo {
        return HomeRepoImpl()
    }

}