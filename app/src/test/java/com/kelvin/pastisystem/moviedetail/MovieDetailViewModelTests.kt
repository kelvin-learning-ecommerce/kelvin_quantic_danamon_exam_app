package com.kelvin.pastisystem.moviedetail

import com.google.common.truth.Truth.assertThat
import com.kelvin.pastisystem.dispatcher.MainDispatcherRule
import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.moviedetail.MovieDetailMockData.Companion.listOfImageData
import com.kelvin.pastisystem.moviedetail.MovieDetailMockData.Companion.movieImageDetailData
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.ui.moviedetail.MovieDetailViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        viewModel = MovieDetailViewModel(movieRepository)
    }

    @Test
    fun `onRequestLoading when movie detail state is empty update isLoading state to true`() = runTest {
        viewModel.updateState()
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.state.value.isLoading

        assertThat(isLoadingState).isTrue()
    }

    @Test
    fun `onRequestLoading when movie detail state is not empty update isLoading state to true`() =
        runTest {
            viewModel.updateState(imageData = listOfImageData, movie = MovieDetailModel())
            viewModel.onRequestLoading()

            val isLoadingState = viewModel.state.value.isLoading

            assertThat(isLoadingState).isTrue()
        }

    @Test
    fun `when error message is not null update error state`() = runTest {
        val errorMsg = "Hi This is an Error"

        viewModel.onRequestError(errorMsg)

        val errorState = viewModel.state.value.error

        assertThat(errorState).isNotEmpty()
    }

    @Test
    fun `onRequestSuccess update the movie detail image list state`() = runTest {
        val testList = listOf(movieImageDetailData, movieImageDetailData)
        viewModel.updateState(imageData = testList)

        viewModel.onRequestSuccess(listOfImageData)

        val movieDetailState = viewModel.state.value.imageData?.plus(testList)
        val expectedList = testList + listOfImageData

        assertThat(movieDetailState).isNotEmpty()
        assertThat(movieDetailState).isEqualTo(expectedList)
    }

    @Test
    fun `when getMovieData called and result is Success`() = runTest {
        val data = List(10) { index -> movieImageDetailData.copy() }

        viewModel.onRequestSuccess(data)

        val actualMovieImageList = viewModel.state.value.imageData

        assertThat(actualMovieImageList).isEqualTo(data)

        viewModel.onRequestSuccess(MovieDetailModel())

        val movieData = viewModel.state.value.data

        assertThat(movieData).isNotNull()
    }
}
