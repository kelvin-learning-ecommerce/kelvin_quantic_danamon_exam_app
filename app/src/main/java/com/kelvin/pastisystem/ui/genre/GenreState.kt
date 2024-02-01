package com.kelvin.pastisystem.ui.genre

import com.kelvin.pastisystem.model.Genres

data class GenreState(
    val genreList: List<Genres> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
