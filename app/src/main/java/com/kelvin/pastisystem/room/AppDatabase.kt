package com.kelvin.pastisystem.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kelvin.pastisystem.room.model.MovieDaoModel

@Database(entities = [MovieDaoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
