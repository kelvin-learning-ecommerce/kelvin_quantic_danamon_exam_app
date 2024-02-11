package com.kelvinquantic.danamon.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kelvinquantic.danamon.room.model.FavoriteDaoModel
import com.kelvinquantic.danamon.room.model.SessionDaoModel
import com.kelvinquantic.danamon.room.model.UserDaoModel

@Dao
interface DaoService {
    @Insert
    fun insertLoginData(data: SessionDaoModel)

    @Query("SELECT * FROM SessionDaoModel")
    fun getLoginData(): List<SessionDaoModel>

    @Query("DELETE FROM SessionDaoModel WHERE username = :username")
    fun deleteLoginData(username: String)

    @Query("SELECT * FROM favoritedaomodel")
    fun getAllPhoto(): List<FavoriteDaoModel>

    @Query("SELECT * FROM userdaomodel WHERE email = :email OR username = :username")
    fun findUser(email: String, username: String): List<UserDaoModel>

    @Query("SELECT * FROM userdaomodel ")
    fun getAllUser(): List<UserDaoModel>

    @Query("UPDATE userdaomodel SET username=:username, email=:email,password=:password,role=:role WHERE username = :currUsername")
    fun updateUser(
        currUsername: String,
        username: String,
        email: String,
        password: String,
        role: String
    )

    @Query("SELECT * FROM userdaomodel WHERE email = :email")
    fun findUserByEmail(email: String): List<UserDaoModel>

    @Query("SELECT * FROM userdaomodel WHERE username = :username")
    fun findUserByUsername(username: String): List<UserDaoModel>

    @Insert
    fun insertPhoto(vararg photo: FavoriteDaoModel)

    @Insert
    fun insertUser(vararg user: UserDaoModel)

    @Query("DELETE FROM favoritedaomodel WHERE id = :id")
    fun deleteByPhotoId(id: Int)
}
