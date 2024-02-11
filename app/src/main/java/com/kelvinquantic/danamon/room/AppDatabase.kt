package com.kelvinquantic.danamon.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kelvinquantic.danamon.room.daomodel.FavoriteDaoModel
import com.kelvinquantic.danamon.room.daomodel.SessionDaoModel
import com.kelvinquantic.danamon.room.daomodel.UserDaoModel

@Database(entities = [FavoriteDaoModel::class, UserDaoModel::class, SessionDaoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): DaoService
}
