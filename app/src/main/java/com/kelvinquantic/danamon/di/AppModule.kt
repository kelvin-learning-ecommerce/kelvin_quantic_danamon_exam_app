package com.kelvinquantic.danamon.di

import com.kelvinquantic.danamon.navigation.AppNavigator
import com.kelvinquantic.danamon.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator
}
