package com.fabiotiago.storytail.di

import com.fabiotiago.storytail.Manager
import com.fabiotiago.storytail.ManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindManager(
        managerImpl: ManagerImpl
    ): Manager
}