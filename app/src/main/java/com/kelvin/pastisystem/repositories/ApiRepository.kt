package com.kelvin.pastisystem.repositories

import com.kelvin.pastisystem.api.MovieService
import com.kelvin.pastisystem.model.Genres
import com.kelvin.pastisystem.model.MovieDetailImageModel
import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.Results
import com.kelvin.pastisystem.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MovieRepository {
    fun getMovieList(page: Int? = 1, genreId: Int? = 28): Flow<Resource<List<Results>>>
    fun getGenre(): Flow<Resource<List<Genres>>>
    fun getMovieDetail(id: Int?): Flow<Resource<MovieDetailModel>>
    fun getMovieDetailImage(id: Int): Flow<Resource<MovieDetailImageModel>>

}

class MovieRepositoryImpl @Inject constructor(private val movieService: MovieService) :
    MovieRepository {

    override fun getMovieList(page: Int?, genreId: Int?): Flow<Resource<List<Results>>> {
        return movieService.getMovieList(page, genreId)
    }

    override fun getGenre(): Flow<Resource<List<Genres>>> {
        return movieService.getGenre()
    }

    override fun getMovieDetail(id: Int?): Flow<Resource<MovieDetailModel>> {
        return movieService.getMovieDetail(id ?: 0)
    }

    override fun getMovieDetailImage(id: Int): Flow<Resource<MovieDetailImageModel>> {
        return movieService.getMovieDetailImage(id)
    }
}
