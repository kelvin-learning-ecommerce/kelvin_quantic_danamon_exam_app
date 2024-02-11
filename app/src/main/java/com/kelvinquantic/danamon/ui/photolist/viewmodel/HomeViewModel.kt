package com.kelvinquantic.danamon.ui.photolist.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelvinquantic.danamon.model.PhotoResponse
import com.kelvinquantic.danamon.model.PhotoUIModel
import com.kelvinquantic.danamon.model.updateIsFavorite
import com.kelvinquantic.danamon.network.Resource
import com.kelvinquantic.danamon.repositories.ApiRepository
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.room.daomodel.FavoriteDaoModel
import com.kelvinquantic.danamon.room.daomodel.SessionDaoModel
import com.kelvinquantic.danamon.room.daomodel.UserDaoModel
import com.kelvinquantic.danamon.ui.photolist.components.PaginationState
import com.kelvinquantic.danamon.ui.photolist.state.HomeState
import com.kelvinquantic.danamon.ui.register.viewmodel.Role
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
class HomeViewModel @Inject constructor(
    private val movieRepository: ApiRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _state = MutableStateFlow(emptyList<UserDaoModel>())
    val userState: StateFlow<List<UserDaoModel>> get() = _state

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    var page: Int = 0
    var isFavorite: Boolean = false
    var userSessionData: SessionDaoModel = SessionDaoModel()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = roomRepository.getLoginData()
            if (data.isNotEmpty()) {
                userSessionData = data.first()

                checkRole()
            }
        }
    }

    fun checkRole() {
        if (userSessionData.role == Role.Admin.toString()) {
            getUserList()
        } else {
            getPhotoList()
        }
    }

    internal fun getPhotoList() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getPhotoList(page + 1)
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

    fun logout(): Boolean{
        userSessionData.username?.let {
            roomRepository.deleteLoginData(it)
            return true
        }

        return false
    }

    internal fun getRoomPhotoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = roomRepository.getAllPhoto()

            val uiData = mutableListOf<PhotoUIModel>()

            data.forEach {
                uiData.add(
                    PhotoUIModel(
                        id = it.id,
                        title = it.title,
                        thumbnailUrl = it.thumbnailUrl,
                        url = it.url,
                        albumId = it.albumId,
                        isFavorite = true
                    )
                )
            }

            onRequestSuccess(uiData)

        }
    }
    internal fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = roomRepository.getAllUser()

            _state.update {
                data
            }
        }
    }

    private fun successGetMovieList(data: List<PhotoResponse>) {
        val roomData = roomRepository.getAllPhoto().toMutableList()

        val uiData = mutableListOf<PhotoUIModel>()

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
                PhotoUIModel(
                    id = it.id,
                    title = it.title,
                    thumbnailUrl = it.thumbnailUrl,
                    url = it.url,
                    albumId = it.albumId,
                    isFavorite = isFav
                )
            )
        }

        onRequestSuccess(uiData)
    }

    fun insertPhoto(context: Context, movieData: PhotoUIModel) {
        val daoModel = FavoriteDaoModel(
            id = movieData.id,
            title = movieData.title,
            thumbnailUrl = movieData.thumbnailUrl,
            url = movieData.url,
            albumId = movieData.albumId,
        )
        if (movieData.isFavorite) {
            daoModel.id?.let {
                roomRepository.deleteByPhotoId(
                    it
                )
            }
        } else {
            roomRepository.insertPhoto(
                daoModel
            )
        }

        Toast.makeText(
            context,
            if (movieData.isFavorite) "Success Remove from Favorite list" else "Success Add to Favorite list",
            Toast.LENGTH_SHORT
        ).show()

        val list = _homeState.value.data.toList()

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
            if (_homeState.value.data.isEmpty()) {
                return
            }

            getPhotoList()
        }
    }

    internal fun onRequestSuccess(
        data: List<PhotoUIModel>
    ) {
        page++

        val movieData = _homeState.value.data + data
        _homeState.update {
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
        _homeState.update {
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
        if (_homeState.value.data.isEmpty()) {
            _homeState.update {
                it.copy(
                    isLoading = true
                )
            }
        }

        if (_homeState.value.data.isNotEmpty()) {
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
            _homeState.update { it.copy(data = persistentListOf()) }
            page = 0
            getPhotoList()
            updateRefreshState(false)
        }

    }

    private fun updateRefreshState(
        value: Boolean
    ) = _isRefresh.update { value }

    fun updateState(
        isLoading: Boolean = false,
        movie: List<PhotoUIModel> = emptyList(),
        error: String = ""
    ) {
        _homeState.update {
            it.copy(
                isLoading = isLoading,
                data = movie.toImmutableList(),
                error = error
            )
        }
    }
}
