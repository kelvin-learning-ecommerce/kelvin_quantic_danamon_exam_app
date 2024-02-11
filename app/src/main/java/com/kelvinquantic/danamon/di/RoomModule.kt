package com.kelvinquantic.danamon.di

import android.content.Context
import androidx.room.Room
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.repositories.RoomRepositoryImpl
import com.kelvinquantic.danamon.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "kelvin_danamon_demoapp_db")
            .allowMainThreadQueries().build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDbProductRepository(db: AppDatabase): RoomRepository {
        return RoomRepositoryImpl(db)
    }
}
