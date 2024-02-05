package com.kelvin.pastisystem.di

import android.content.Context
import androidx.room.Room
import com.kelvin.pastisystem.repositories.RoomRepository
import com.kelvin.pastisystem.repositories.RoomRepositoryImpl
import com.kelvin.pastisystem.room.AppDatabase
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
        return Room.databaseBuilder(context, AppDatabase::class.java, "pastisystem_demo_app_db")
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
