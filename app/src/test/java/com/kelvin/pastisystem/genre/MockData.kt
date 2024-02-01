package com.kelvin.pastisystem.genre

import com.kelvin.pastisystem.model.Genres

class MockData {
    companion object {
        val genres = Genres(
            id = 10,
            name = "Action"
        )

        //list of 4 coins
        val listOfGenre = listOf(
            genres, genres, genres, genres
        )
    }
}
