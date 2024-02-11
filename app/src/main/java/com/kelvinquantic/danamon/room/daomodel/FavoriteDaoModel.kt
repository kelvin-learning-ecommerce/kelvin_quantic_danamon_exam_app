package com.kelvinquantic.danamon.room.daomodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteDaoModel(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo("albumId") var albumId: Int? = null,
    @ColumnInfo("id") var id: Int? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("url") var url: String? = null,
    @ColumnInfo("thumbnailUrl") var thumbnailUrl: String? = null
)
