package com.kelvin.pastisystem.moviedetail

import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.Posters

class MovieDetailMockData {
    companion object {
        val movieDetailData = MovieDetailModel()
        val movieImageDetailData = Posters()

        //list of 4 coins
        val listOfImageData = listOf(
            movieImageDetailData, movieImageDetailData, movieImageDetailData, movieImageDetailData
        )
    }
}
