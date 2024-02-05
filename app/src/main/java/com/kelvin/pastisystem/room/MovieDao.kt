package com.kelvin.pastisystem.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kelvin.pastisystem.room.model.MovieDaoModel

@Dao
interface MovieDao {
    @Query("SELECT * FROM moviedaomodel")
    fun getAll(): List<MovieDaoModel>

    @Query("SELECT * FROM moviedaomodel WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<MovieDaoModel>

    @Insert
    fun insertMovie(vararg movie: MovieDaoModel)

    @Delete
    fun delete(user: MovieDaoModel)
}
