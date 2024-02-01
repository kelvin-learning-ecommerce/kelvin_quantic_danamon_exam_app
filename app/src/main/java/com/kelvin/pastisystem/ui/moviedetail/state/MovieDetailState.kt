package com.kelvin.pastisystem.ui.moviedetail.state

import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.Posters

data class MovieDetailState(
    val isLoading: Boolean = false,
    val data: MovieDetailModel? = null,
    val imageData: List<Posters>? = emptyList(),
    val error: String = ""
    )
