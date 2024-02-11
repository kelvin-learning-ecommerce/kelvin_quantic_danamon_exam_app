package com.kelvinquantic.danamon.room.daomodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionDaoModel(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo("isLogin") var isLogin: Boolean? = null,
    @ColumnInfo("username") var username: String? = null,
    @ColumnInfo("email") var email: String? = null,
    @ColumnInfo("role") var role: String? = null
)
