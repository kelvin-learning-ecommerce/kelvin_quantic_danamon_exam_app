package com.kelvin.pastisystem.movielist

import com.kelvin.pastisystem.model.Results

class MovieMockData {
    companion object {
        val movie = Results(
            adult = false,
            backdropPath = "/cnqwv5Uz3UW5f086IWbQKr3ksJr.jpg",
            id = 572802,
            originalLanguage = "en",
            originalTitle = "Aquaman and the Lost Kingdom"
        )

        //list of 4 coins
        val listOfMovie = listOf(
            movie, movie, movie, movie
        )
    }
}
