package com.kelvin.pastisystem.home

import com.google.common.truth.Truth.assertThat
import com.kelvin.pastisystem.dispatcher.MainDispatcherRule
import com.kelvin.pastisystem.home.MovieMockData.Companion.listOfPhotos
import com.kelvin.pastisystem.home.MovieMockData.Companion.photo
import com.kelvinquantic.danamon.repositories.ApiRepository
import com.kelvinquantic.danamon.repositories.RoomRepository
import com.kelvinquantic.danamon.ui.home.viewmodel.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ApiRepository
    private lateinit var roomRepository: RoomRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        roomRepository = mockk(relaxed = true)
        viewModel = HomeViewModel(repository, roomRepository)
    }

    @Test
    fun `onRequestLoading when home state is empty update isLoading state to true`() = runTest {
        viewModel.updateState(movie = emptyList())
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.homeState.value.isLoading

        assertThat(isLoadingState).isTrue()
    }

    @Test
    fun `onRequestLoading when home state is not empty update isLoading pagination state to true`() =
        runTest {
            viewModel.updateState(movie = listOfPhotos)
            viewModel.onRequestLoading()

            val isLoadingState = viewModel.homeState.value.isLoading

            assertThat(isLoadingState).isFalse()
        }

    @Test
    fun `when error message is not null update error state`() = runTest {
        val errorMsg = "Hi This is an Error"

        viewModel.onRequestError(errorMsg)

        val errorState = viewModel.homeState.value.error

        assertThat(errorState).isNotEmpty()
    }

    @Test
    fun `onRequestSuccess update the home state`() = runTest {
        val testList = listOf(photo, photo)
        viewModel.updateState(movie = testList)

        viewModel.onRequestSuccess(listOfPhotos)

        val movieState = viewModel.homeState.value.data
        val expectedList = testList + listOfPhotos

        assertThat(movieState).isNotEmpty()
        assertThat(movieState).isEqualTo(expectedList)
    }

    @Test
    fun `onRequestSuccess update pagination state when data is not empty or didn't reached the limit`() = runTest {
        viewModel.onRequestSuccess(listOfPhotos)

        val actualIsLoadingState = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `onRequestSuccess update pagination state when data is empty`() = runTest {
        viewModel.updateState(movie = listOfPhotos)
        viewModel.onRequestSuccess(emptyList())

        val actualIsLoadingState = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `when getPhoto called and result is Success`() = runTest {
        val data = List(10) { index -> photo.copy() }

        viewModel.onRequestSuccess(data)

        val actualmovieList = viewModel.homeState.value.data
        val actualIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualmovieList).isEqualTo(data)
        assertThat(actualIsLoading).isFalse()
    }

    @Test
    fun `when getPhoto called and result is Error`() = runTest {
        val errorMsg = "Error occurred"
        viewModel.onRequestError(errorMsg)

        val actualError = viewModel.homeState.value.error

        assertThat(actualError).isNotEmpty()
        assertThat(actualError).isEqualTo(errorMsg)
    }

    @Test
    fun `when getPhoto called and result is Loading when home state is empty`() = runTest {
        viewModel.updateState()

        viewModel.onRequestLoading()

        val actualIsLoading = viewModel.homeState.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isTrue()
        assertThat(actualPaginationIsLoading).isFalse()
    }

    @Test
    fun `when getPhoto called and result is Loading when movie state is not empty`() = runTest {
        viewModel.updateState(movie = listOfPhotos)

        viewModel.onRequestLoading()

        val actualIsLoading = viewModel.homeState.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isFalse()
        assertThat(actualPaginationIsLoading).isTrue()
    }
}
