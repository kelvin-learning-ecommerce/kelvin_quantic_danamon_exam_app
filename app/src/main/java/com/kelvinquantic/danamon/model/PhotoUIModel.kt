package com.kelvinquantic.danamon.model

data class PhotoUIModel(
    var albumId: Int? = null,
    var id: Int? = null,
    var title: String? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null,
    var isFavorite: Boolean = false
)

fun List<PhotoUIModel>.updateIsFavorite(id: Int, isFav: Boolean): List<PhotoUIModel> =
    this.map { worker -> if (worker.id == id) worker.copy(isFavorite = isFav) else worker }
