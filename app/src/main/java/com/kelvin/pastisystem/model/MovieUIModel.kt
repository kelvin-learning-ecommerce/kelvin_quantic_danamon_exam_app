package com.kelvin.pastisystem.model

data class MovieUIModel(
    var id: Int? = null,
    var title: String? = null,
    var overview: String? = null,
    var releaseDate: String? = null,
    var voteAverage: Any? = null,
    var voteCount: Int? = null,
    var posterPath: String? = null,
)
