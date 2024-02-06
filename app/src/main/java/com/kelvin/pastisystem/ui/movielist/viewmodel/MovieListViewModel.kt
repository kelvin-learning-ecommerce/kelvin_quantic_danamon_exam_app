package com.kelvin.pastisystem.ui.movielist.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvin.pastisystem.model.MovieUIModel
import com.kelvin.pastisystem.model.Results
import com.kelvin.pastisystem.model.updateIsFavorite
import com.kelvin.pastisystem.network.Resource
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.repositories.RoomRepository
import com.kelvin.pastisystem.room.model.MovieDaoModel
import com.kelvin.pastisystem.ui.movielist.state.MovieListState
import com.kelvin.pastisystem.ui.movielist.state.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state = _state.asStateFlow()

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    var page: Int = 0
    var genreId: Int = 0
    var isFavorite: Boolean = false

    internal fun getMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getMovieList(page + 1, genreId)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> result.data?.let { data ->
                            successGetMovieList(data)
                        }

                        is Resource.Error -> onRequestError(result.message)
                        is Resource.Loading -> onRequestLoading()
                    }
                }
        }
    }

    private fun successGetMovieList(data: List<Results>) {
        val roomData = roomRepository.getAll().toMutableList()

        val uiData = mutableListOf<MovieUIModel>()

        data.forEach {
            var isFav = false
            if (roomData.isNotEmpty()) {
                val found = roomData.indexOfFirst { dao ->
                    dao.id == it.id
                }
                if (found >= 0) {
                    isFav = true
                    roomData.removeAt(found)
                }
            }
            uiData.add(
                MovieUIModel(
                    id = it.id,
                    title = it.title,
                    overview = it.overview,
                    releaseDate = it.releaseDate,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    posterPath = it.posterPath,
                    isFavorite = isFav
                )
            )
        }

        onRequestSuccess(uiData)
    }

    fun getRoomMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = roomRepository.getAll()

            val uiData = data.map {
                MovieUIModel(
                    id = it.id,
                    title = it.title,
                    overview = it.overview,
                    releaseDate = it.releaseDate,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    posterPath = it.posterPath
                )
            }

            onRequestSuccess(uiData)
        }
    }

    fun insertMovie(context: Context, movieData: MovieUIModel) {
        val daoModel = MovieDaoModel(
            id = movieData.id,
            title = movieData.title,
            overview = movieData.overview,
            releaseDate = movieData.releaseDate,
            voteAverage = movieData.voteAverage.toString(),
            voteCount = movieData.voteCount,
            posterPath = movieData.posterPath
        )
        if (movieData.isFavorite) {
            daoModel.id?.let {
                roomRepository.deleteByMovieId(
                    it
                )
            }
        } else {
            roomRepository.insertMovie(
                daoModel
            )
        }

        Toast.makeText(
            context,
            if (movieData.isFavorite) "Success Remove from Favorite list" else "Success Add to Favorite list",
            Toast.LENGTH_SHORT
        ).show()

        val list = _state.value.data.toList()

        val uiData = list.updateIsFavorite(movieData.id ?: 0, !movieData.isFavorite)

        updateState(
            isLoading = false,
            movie = uiData,
        )

        _paginationState.update {
            it.copy(
                isLoading = false
            )
        }
    }

    fun getCoinsPaginated() {
        if (!isFavorite) {
            if (_state.value.data.isEmpty()) {
                return
            }

            getMovieList()
        }
    }

    internal fun onRequestSuccess(
        data: List<MovieUIModel>
    ) {
        page++

        val movieData = _state.value.data + data
        _state.update {
            it.copy(
                data = movieData.toImmutableList(),
                isLoading = false,
                error = ""
            )
        }

        if (!isFavorite) {
            _paginationState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    internal fun onRequestError(
        message: String?
    ) {
        _state.update {
            it.copy(
                error = message ?: "Unexpected Error",
                isLoading = false,
            )
        }

        _paginationState.update {
            it.copy(
                isLoading = false
            )
        }
    }

    internal fun onRequestLoading() {
        if (_state.value.data.isEmpty()) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
        }

        if (_state.value.data.isNotEmpty()) {
            _paginationState.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            updateRefreshState(true)
            _state.update { it.copy(data = persistentListOf()) }
            page = 0
            getMovieList()
            updateRefreshState(false)
        }

    }

    private fun updateRefreshState(
        value: Boolean
    ) = _isRefresh.update { value }

    fun updateState(
        isLoading: Boolean = false,
        movie: List<MovieUIModel> = emptyList(),
        error: String = ""
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading,
                data = movie.toImmutableList(),
                error = error
            )
        }
    }
}
