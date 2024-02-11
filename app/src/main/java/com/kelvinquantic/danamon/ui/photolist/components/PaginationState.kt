package com.kelvinquantic.danamon.ui.photolist.components

data class PaginationState(
    val isLoading: Boolean = false,
    val skip: Int = 1,
    val endReached: Boolean = false
)
