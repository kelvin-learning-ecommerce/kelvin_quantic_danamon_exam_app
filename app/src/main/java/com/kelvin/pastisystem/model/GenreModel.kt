package com.kelvin.pastisystem.model

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("genres") var genres: ArrayList<Genres> = arrayListOf()
)

data class Genres(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)
