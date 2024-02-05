package com.kelvin.pastisystem.ui.movielist.components

import android.content.Intent
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kelvin.pastisystem.R
import com.kelvin.pastisystem.model.MovieUIModel
import com.kelvin.pastisystem.ui.common.InfiniteListHandler
import com.kelvin.pastisystem.ui.common.ScrollButton
import com.kelvin.pastisystem.ui.common.findActivity
import com.kelvin.pastisystem.ui.moviedetail.MovieDetailActivity
import com.kelvin.pastisystem.ui.movielist.MovieListActivity
import com.kelvin.pastisystem.ui.movielist.state.CoinsScreenState
import com.kelvin.pastisystem.ui.movielist.viewmodel.MovieListViewModel

@ExperimentalComposeUiApi
@Composable
fun MovieListScreen() {
    val viewModel = hiltViewModel<MovieListViewModel>()

    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent

    viewModel.genreId = intent?.getIntExtra("id", 1) ?: 1
    viewModel.isFavorite = intent?.getBooleanExtra("isFavorite", false) ?: false

    if (viewModel.isFavorite) {
        viewModel.getRoomMovieList()
    } else {
        viewModel.getMovieList()
    }

    val state by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val paginationState by viewModel.paginationState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val focusManger = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, MovieListActivity::class.java)
                intent.putExtra("id", viewModel.genreId)
                intent.putExtra("isFavorite", true)
                context.startActivity(intent)
            }) {
                Card(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                ) {
                    Image(
                        painterResource(R.drawable.ic_mlkit),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                        onRefresh = {
                            viewModel.refresh()
                        }
                    ) {
                        Content(
                            modifier = Modifier
                                .pointerInteropFilter {
                                    when (it.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            focusManger.clearFocus()
                                        }

                                        MotionEvent.ACTION_UP -> {
                                            focusManger.clearFocus()
                                        }
                                    }
                                    false
                                },
                            lazyListState = lazyListState,
                            data = state.data,
                            onItemFav = {
                                viewModel.insertMovie(it)
                            }
                        )

                        InfiniteListHandler(lazyListState = lazyListState) {
                            viewModel.getCoinsPaginated()
                        }
                    }

                }

                CoinsScreenState()
            }

            if (paginationState.isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            ScrollButton(lazyListState = lazyListState)
        }
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    data: List<MovieUIModel>,
    onItemFav: (MovieUIModel) -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(
            items = data,
//            key = { it.id ?: 0 }
        ) { movie ->
            MovieListItem(
                data = movie,
                onItemClick = {
                    val intent = Intent(context, MovieDetailActivity::class.java)
                    intent.putExtra("id", it.id)
                    context.startActivity(intent)
                },
                onItemFav = {
                    onItemFav(it)
                }
            )
        }
    }
}
