package com.kelvin.pastisystem.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.Posters
import com.kelvin.pastisystem.network.Resource
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.ui.moviedetail.state.MovieDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepositoryImpl: MovieRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()

    fun getMovieDetail(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepositoryImpl.getMovieDetail(id ?: 0)
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

    fun getMovieDetailImage(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepositoryImpl.getMovieDetailImage(id)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> result.data?.let { data -> onRequestSuccess(data.posters) }
                        is Resource.Error -> onRequestError(result.message)
                        is Resource.Loading -> onRequestLoading(isLoading = false)
                    }
                }
        }
    }

    fun onRequestSuccess(data: MovieDetailModel?) {
        _state.update {
            it.copy(
                data = data,
                isLoading = false,
                error = ""
            )
        }
    }

    fun onRequestSuccess(data: List<Posters>?) {
        _state.update {
            it.copy(
                imageData = data,
                isLoading = false,
                error = ""
            )
        }
    }

    fun onRequestError(
        message: String?
    ) {
        _state.update {
            it.copy(
                error = message ?: "Unexpected Error",
                isLoading = false,
            )
        }
    }

    fun onRequestLoading(isLoading: Boolean = true) {
        _state.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    fun updateState(
        isLoading: Boolean = false,
        movie: MovieDetailModel? = null,
        imageData: List<Posters>? = null,
        error: String = ""
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading,
                data = movie,
                imageData = imageData,
                error = error
            )
        }
    }
}
