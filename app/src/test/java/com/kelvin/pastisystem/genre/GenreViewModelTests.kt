package com.kelvin.pastisystem.genre

import com.google.common.truth.Truth.assertThat
import com.kelvin.pastisystem.dispatcher.MainDispatcherRule
import com.kelvin.pastisystem.genre.MockData.Companion.genres
import com.kelvin.pastisystem.genre.MockData.Companion.listOfGenre
import com.kelvin.pastisystem.network.Resource
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.ui.genre.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GenreViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        viewModel = HomeViewModel(movieRepository)
    }

    @Test
    fun `onRequestLoading when genre state is empty update isLoading state to true`() = runTest {
        viewModel.updateState(genres = emptyList())
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.genreState.value.isLoading

        assertThat(isLoadingState).isTrue()
    }

    @Test
    fun `onRequestLoading when genre state is not empty update isLoading pagination state to true`() =
        runTest {
            viewModel.updateState(genres = listOfGenre)
            viewModel.onRequestLoading()

            val isLoadingState = viewModel.genreState.value.isLoading

            assertThat(isLoadingState).isFalse()
        }

    @Test
    fun `when error message is not null update error state`() = runTest {
        val errorMsg = "Hi This is an Error"

        viewModel.onRequestError(errorMsg)

        val errorState = viewModel.genreState.value.error

        assertThat(errorState).isNotEmpty()
    }

    @Test
    fun `onRequestSuccess update the genre list state`() = runTest {
        val testList = listOf(genres, genres)
        viewModel.updateState(genres = testList)

        viewModel.onRequestSuccess(listOfGenre)

        val genreState = viewModel.genreState.value.genreList + testList
        val expectedList = testList + listOfGenre

        assertThat(genreState).isNotEmpty()
        assertThat(genreState).isEqualTo(expectedList)
    }

//    @Test
//    fun `onRequestSuccess update pagination state when data is not empty or didn't reached the limit`() = runTest {
//        viewModel.onRequestSuccess(listOfCoins)
//
//        val actualSkip = viewModel.paginationState.value.skip //4 coins
//        val actualEndReached = viewModel.paginationState.value.endReached // false -> data is not empty OR didn't reach limit
//        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false
//
//        assertThat(actualSkip).isEqualTo(listOfCoins.size)
//        assertThat(actualEndReached).isFalse()
//        assertThat(actualIsLoadingState).isFalse()
//    }

//    @Test
//    fun `onRequestSuccess update pagination state when data is empty`() = runTest {
//        viewModel.updateState(coins = listOfCoins)
//        viewModel.onRequestSuccess(emptyList())
//
//        val actualSkip = viewModel.paginationState.value.skip //4 coins
//        val actualEndReached = viewModel.paginationState.value.endReached // true -> data is empty
//        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false
//
//        assertThat(actualSkip).isEqualTo(listOfCoins.size)
//        assertThat(actualEndReached).isTrue()
//        assertThat(actualIsLoadingState).isFalse()
//    }

//    @Test
//    fun `onRequestSuccess update pagination state when reached the limit`() = runTest {
//        val testList = List(400) { index -> coins.copy(uniqueId = index.toString()) }
//
//        viewModel.updateState(coins = testList)
//        viewModel.onRequestSuccess(listOfCoins)
//
//        val coinsState = viewModel.state.value.coins
//
//        val actualSkip = viewModel.paginationState.value.skip //404 coins
//        val actualEndReached = viewModel.paginationState.value.endReached // true -> limitReached
//        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false
//
//        assertThat(actualSkip).isEqualTo(coinsState.size)
//        assertThat(actualEndReached).isTrue()
//        assertThat(actualIsLoadingState).isFalse()
//    }

    @Test
    fun `when getgenre called and result is Success`() = runTest {
//        val skip = viewModel.paginationState.value.skip
        val data = List(10) { index -> genres.copy() }

        every { movieRepository.getGenre() } returns flowOf(Resource.Success(data))
        viewModel.getGenreList()

        val actualgenreList = viewModel.genreState.value.genreList
//        val actualSkip = viewModel.paginationState.value.skip
//        val actualEndReached = viewModel.paginationState.value.endReached
//        val actualIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualgenreList).isEqualTo(data)
//        assertThat(actualSkip).isEqualTo(actualCoinsList.size)
//        assertThat(actualEndReached).isFalse()
//        assertThat(actualIsLoading).isFalse()
    }

    @Test
    fun `when getgenre called and result is Error`() = runTest {
//        val skip = viewModel.paginationState.value.skip

        val errorMsg = "Error occurred"
        every { movieRepository.getGenre() } returns flowOf(Resource.Error(errorMsg))
        viewModel.getGenreList()

        val actualError = viewModel.genreState.value.error

        assertThat(actualError).isNotEmpty()
        assertThat(actualError).isEqualTo(errorMsg)
    }

    @Test
    fun `when getgenre called and result is Loading when genre state is empty`() = runTest {
//        val skip = viewModel.paginationState.value.skip
        viewModel.updateState()

        every { movieRepository.getGenre() } returns flowOf(Resource.Loading())
        viewModel.getGenreList()

        val actualIsLoading = viewModel.genreState.value.isLoading
//        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isTrue()
//        assertThat(actualPaginationIsLoading).isFalse()
    }

    @Test
    fun `when getgenre called and result is Loading when genre state is not empty`() = runTest {
//        val skip = viewModel.paginationState.value.skip
        viewModel.updateState(genres = listOfGenre)

        every { movieRepository.getGenre() } returns flowOf(Resource.Loading())
        viewModel.getGenreList()

        val actualIsLoading = viewModel.genreState.value.isLoading
//        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isFalse()
//        assertThat(actualPaginationIsLoading).isTrue()
    }
}
