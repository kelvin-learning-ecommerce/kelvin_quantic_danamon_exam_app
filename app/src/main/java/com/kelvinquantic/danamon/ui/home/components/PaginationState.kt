package com.kelvinquantic.danamon.ui.home.components

data class PaginationState(
    val isLoading: Boolean = false,
    val skip: Int = 1,
    val endReached: Boolean = false
)
