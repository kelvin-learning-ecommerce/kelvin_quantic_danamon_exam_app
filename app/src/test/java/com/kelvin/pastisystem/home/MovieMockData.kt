package com.kelvin.pastisystem.home

import com.kelvinquantic.danamon.model.PhotoUIModel

class MovieMockData {
    companion object {
        val photo = PhotoUIModel(
            id = 572802,
            title = "Aquaman and the Lost Kingdom"
        )

        //list of 4 coins
        val listOfPhotos = listOf(
            photo, photo, photo, photo
        )
    }
}
