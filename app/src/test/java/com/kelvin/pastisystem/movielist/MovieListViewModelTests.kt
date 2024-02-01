package com.kelvin.pastisystem.movielist

import com.google.common.truth.Truth.assertThat
import com.kelvin.pastisystem.dispatcher.MainDispatcherRule
import com.kelvin.pastisystem.movielist.MovieMockData.Companion.listOfMovie
import com.kelvin.pastisystem.movielist.MovieMockData.Companion.movie
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.ui.movielist.viewmodel.MovieListViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieListViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        viewModel = MovieListViewModel(movieRepository)
    }

    @Test
    fun `onRequestLoading when movie state is empty update isLoading state to true`() = runTest {
        viewModel.updateState(movie = emptyList())
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.state.value.isLoading

        assertThat(isLoadingState).isTrue()
    }

    @Test
    fun `onRequestLoading when movie state is not empty update isLoading pagination state to true`() =
        runTest {
            viewModel.updateState(movie = listOfMovie)
            viewModel.onRequestLoading()

            val isLoadingState = viewModel.state.value.isLoading

            assertThat(isLoadingState).isFalse()
        }

    @Test
    fun `when error message is not null update error state`() = runTest {
        val errorMsg = "Hi This is an Error"

        viewModel.onRequestError(errorMsg)

        val errorState = viewModel.state.value.error

        assertThat(errorState).isNotEmpty()
    }

    @Test
    fun `onRequestSuccess update the movie list state`() = runTest {
        val testList = listOf(movie, movie)
        viewModel.updateState(movie = testList)

        viewModel.onRequestSuccess(listOfMovie)

        val movieState = viewModel.state.value.data
        val expectedList = testList + listOfMovie

        assertThat(movieState).isNotEmpty()
        assertThat(movieState).isEqualTo(expectedList)
    }

    @Test
    fun `onRequestSuccess update pagination state when data is not empty or didn't reached the limit`() = runTest {
        viewModel.onRequestSuccess(listOfMovie)

        val actualIsLoadingState = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `onRequestSuccess update pagination state when data is empty`() = runTest {
        viewModel.updateState(movie = listOfMovie)
        viewModel.onRequestSuccess(emptyList())

        val actualIsLoadingState = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `when getMovie called and result is Success`() = runTest {
        val data = List(10) { index -> movie.copy() }

        viewModel.onRequestSuccess(data)

        val actualmovieList = viewModel.state.value.data
        val actualIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualmovieList).isEqualTo(data)
        assertThat(actualIsLoading).isFalse()
    }

    @Test
    fun `when getMovie called and result is Error`() = runTest {
        val errorMsg = "Error occurred"
        viewModel.onRequestError(errorMsg)

        val actualError = viewModel.state.value.error

        assertThat(actualError).isNotEmpty()
        assertThat(actualError).isEqualTo(errorMsg)
    }

    @Test
    fun `when getMovie called and result is Loading when movie state is empty`() = runTest {
        viewModel.updateState()

        viewModel.onRequestLoading()

        val actualIsLoading = viewModel.state.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isTrue()
        assertThat(actualPaginationIsLoading).isFalse()
    }

    @Test
    fun `when getMovie called and result is Loading when movie state is not empty`() = runTest {
        viewModel.updateState(movie = listOfMovie)

        viewModel.onRequestLoading()

        val actualIsLoading = viewModel.state.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isFalse()
        assertThat(actualPaginationIsLoading).isTrue()
    }
}
