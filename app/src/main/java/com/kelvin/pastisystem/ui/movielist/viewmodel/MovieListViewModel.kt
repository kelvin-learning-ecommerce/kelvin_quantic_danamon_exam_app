package com.kelvin.pastisystem.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvin.pastisystem.model.Results
import com.kelvin.pastisystem.network.Resource
import com.kelvin.pastisystem.repositories.MovieRepository
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
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state = _state.asStateFlow()

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    var page: Int = 0
    var genreId: Int = 0

    internal fun getMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getMovieList(page + 1, genreId)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> result.data?.let { data -> onRequestSuccess(data) }
                        is Resource.Error -> onRequestError(result.message)
                        is Resource.Loading -> onRequestLoading()
                        else -> Unit
                    }
                }
        }
    }

    fun getCoinsPaginated() {
        if (_state.value.data.isEmpty()) {
            return
        }

        getMovieList()
    }

    internal fun onRequestSuccess(
        data: List<Results>
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

        _paginationState.update {
            it.copy(
                isLoading = false
            )
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
        movie: List<Results> = emptyList(),
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
