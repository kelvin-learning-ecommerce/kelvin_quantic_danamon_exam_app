package com.kelvin.pastisystem.ui.movielist.components

data class PaginationState(
    val isLoading: Boolean = false,
    val skip: Int = 1,
    val endReached: Boolean = false
)
