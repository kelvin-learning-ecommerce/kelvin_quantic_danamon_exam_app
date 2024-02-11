package com.kelvinquantic.danamon.repositories

import com.kelvinquantic.danamon.room.AppDatabase
import com.kelvinquantic.danamon.room.model.FavoriteDaoModel
import com.kelvinquantic.danamon.room.model.SessionDaoModel
import com.kelvinquantic.danamon.room.model.UserDaoModel
import javax.inject.Inject

interface RoomRepository {
    fun insertLoginData(data: SessionDaoModel)
    fun getLoginData(): List<SessionDaoModel>
    fun deleteLoginData(username: String)
    fun deleteUser(username: String)

    fun getAllPhoto(): List<FavoriteDaoModel>
    fun findUserByEmail(email: String): List<UserDaoModel>
    fun findUserByUsername(username: String): List<UserDaoModel>
    fun insertPhoto(photo: FavoriteDaoModel)
    fun insertUser(user: UserDaoModel)
    fun updateUser(
        currUsername: String,
        username: String,
        email: String,
        password: String,
        role: String
    )

    fun deleteByPhotoId(id: Int)
    fun findUser(email: String, username: String): List<UserDaoModel>
    fun getAllUser(): List<UserDaoModel>


}

class RoomRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    RoomRepository {
    override fun insertLoginData(data: SessionDaoModel) {
        return appDatabase.movieDao().insertLoginData(data)

    }

    override fun getLoginData(): List<SessionDaoModel> {
        return appDatabase.movieDao().getLoginData()

    }

    override fun deleteLoginData(username: String) {
        return appDatabase.movieDao().deleteLoginData(username)

    }

    override fun deleteUser(username: String) {
        return appDatabase.movieDao().deleteUserData(username)

    }

    override fun getAllPhoto(): List<FavoriteDaoModel> {
        return appDatabase.movieDao().getAllPhoto()
    }

    override fun findUserByEmail(email: String): List<UserDaoModel> {
        return appDatabase.movieDao().findUserByEmail(email)

    }

    override fun findUserByUsername(username: String): List<UserDaoModel> {
        return appDatabase.movieDao().findUserByUsername(username)
    }

    override fun findUser(email: String, username: String): List<UserDaoModel> {
        return appDatabase.movieDao().findUser(email, username)

    }

    override fun getAllUser(): List<UserDaoModel> {
        return appDatabase.movieDao().getAllUser()

    }

    override fun insertPhoto(photo: FavoriteDaoModel) {
        return appDatabase.movieDao().insertPhoto(photo)
    }

    override fun insertUser(user: UserDaoModel) {
        return appDatabase.movieDao().insertUser(user)

    }

    override fun updateUser(
        currUsername: String,
        username: String,
        email: String,
        password: String,
        role: String
    ) {
        return appDatabase.movieDao().updateUser(currUsername, username, email, password, role)

    }

    override fun deleteByPhotoId(id: Int) {
        return appDatabase.movieDao().deleteByPhotoId(id)

    }
}
