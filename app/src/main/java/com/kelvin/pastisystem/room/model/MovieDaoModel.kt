package com.kelvin.pastisystem.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDaoModel(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo("id") var id: Int? = null,
    @ColumnInfo("overview") var overview: String? = null,
    @ColumnInfo("poster_path") var posterPath: String? = null,
    @ColumnInfo("release_date") var releaseDate: String? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("vote_average") var voteAverage: String? = null,
    @ColumnInfo("vote_count") var voteCount: Int? = null
)
