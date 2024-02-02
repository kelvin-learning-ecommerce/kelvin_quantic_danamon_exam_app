package com.kelvin.pastisystem.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvin.pastisystem.model.Genres
import com.kelvin.pastisystem.network.Resource
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.ui.home.state.GenreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _state = MutableStateFlow(GenreState())
    val genreState: StateFlow<GenreState> get() = _state

    init {
        getGenreList()
    }

    fun getGenreList() {
        viewModelScope.launch {
            movieRepository.getGenre()
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> result.data?.let { data -> onRequestSuccess(data) }
                        is Resource.Error -> onRequestError(result.message)
                        is Resource.Loading -> onRequestLoading()
                    }
                }
        }
    }

    internal fun onRequestSuccess(
        data: List<Genres>
    ) {
        _state.value = _state.value.copy(
            genreList = data,
            isLoading = false
        )
    }

    internal fun onRequestLoading() {
        if (_state.value.genreList.isEmpty()) {
            _state.update {
                it.copy(
                    isLoading = true
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
    }

    fun updateState(
        isLoading: Boolean = false,
        genres: List<Genres> = emptyList(),
        error: String = ""
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading,
                genreList = genres.toImmutableList(),
                error = error
            )
        }
    }
}
