package com.kelvinquantic.danamon.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kelvinquantic.danamon.room.model.FavoriteDaoModel
import com.kelvinquantic.danamon.room.model.SessionDaoModel
import com.kelvinquantic.danamon.room.model.UserDaoModel

@Database(entities = [FavoriteDaoModel::class, UserDaoModel::class, SessionDaoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): DaoService
}
