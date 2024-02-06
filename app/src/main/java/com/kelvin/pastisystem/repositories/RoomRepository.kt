package com.kelvin.pastisystem.repositories

import com.kelvin.pastisystem.room.AppDatabase
import com.kelvin.pastisystem.room.model.MovieDaoModel
import javax.inject.Inject

interface RoomRepository {
    fun getAll(): List<MovieDaoModel>
    fun loadAllByIds(userIds: IntArray): List<MovieDaoModel>
    fun insertMovie(vararg movie: MovieDaoModel)
    fun delete(user: MovieDaoModel)
    fun deleteByMovieId(id: Int)


}
class RoomRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase): RoomRepository {
    override fun getAll(): List<MovieDaoModel> {
        return appDatabase.movieDao().getAll()
    }

    override fun loadAllByIds(userIds: IntArray): List<MovieDaoModel> {
        return appDatabase.movieDao().loadAllByIds(userIds)

    }

    override fun insertMovie(vararg movie: MovieDaoModel) {
        return appDatabase.movieDao().insertMovie(movie = movie)

    }

    override fun delete(user: MovieDaoModel) {
        return appDatabase.movieDao().delete(user)

    }

    override fun deleteByMovieId(id: Int) {
        return appDatabase.movieDao().deleteByMovieId(id)
    }
}
