package com.kelvin.pastisystem.model

data class MovieUIModel(
    var id: Int? = null,
    var title: String? = null,
    var overview: String? = null,
    var releaseDate: String? = null,
    var voteAverage: Any? = null,
    var voteCount: Int? = null,
    var posterPath: String? = null,
    var isFavorite: Boolean = false
)

fun List<MovieUIModel>.updateIsFavorite(id: Int, isFav: Boolean): List<MovieUIModel> =
    this.map { worker -> if (worker.id == id) worker.copy(isFavorite = isFav) else worker }
