package com.kelvin.pastisystem.model

import com.google.gson.annotations.SerializedName

data class MovieDetailImageModel(
    @SerializedName("backdrops") var backdrops: ArrayList<Backdrops> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("logos") var logos: ArrayList<Logos> = arrayListOf(),
    @SerializedName("posters") var posters: List<Posters> = arrayListOf()
)

data class Backdrops(
    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
//    @SerializedName("vote_average" ) var voteAverage : Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("width") var width: Int? = null
)

data class Logos(
    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
//    @SerializedName("vote_average" ) var voteAverage : Int?    = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("width") var width: Int? = null
)

data class Posters(
    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
//    @SerializedName("vote_average" ) var voteAverage : Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("width") var width: Int? = null
)
