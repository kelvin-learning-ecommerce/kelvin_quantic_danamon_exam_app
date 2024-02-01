package com.kelvin.pastisystem.ui.moviedetail.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.Posters
import com.kelvin.pastisystem.ui.common.findActivity
import com.kelvin.pastisystem.ui.moviedetail.MovieDetailViewModel
import com.kelvin.pastisystem.ui.moviedetail.common.MovieDetailShimmerListItem
import com.kelvin.pastisystem.utils.bigTextStyle
import com.kelvin.pastisystem.utils.mediumTextStyle

private val headerHeight = 250.dp

@Composable
fun CollapsingToolbar(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent

    val id = intent?.getIntExtra("id", 1) ?: 1

    viewModel.getMovieDetail(id)
    viewModel.getMovieDetailImage(id)

    val scroll: ScrollState = rememberScrollState(0)

    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }

    val state = viewModel.state.collectAsState()

    if (state.value.isLoading) {
        MovieDetailShimmerListItem()
    } else {

        Box(modifier = modifier) {
            Header(
                scroll = scroll,
                headerHeightPx = headerHeightPx,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight),
                data = state.value.data
            )
            Body(
                scroll = scroll,
                modifier = Modifier.fillMaxSize(),
                data = state.value.data,
                imageData = state.value.imageData
            )
        }
    }
}

@Composable
private fun Header(
    scroll: ScrollState,
    headerHeightPx: Float,
    modifier: Modifier = Modifier,
    data: MovieDetailModel?
) {
    Column {
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationY = -scroll.value.toFloat() / 2f // Parallax effect
                    alpha = (-1f / headerHeightPx) * scroll.value + 1
                }
        ) {

            AsyncImage(

                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/original/${data?.backdropPath}")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Fit,
                contentDescription = "backdrop image",
                modifier = Modifier
                    .align(Alignment.Center)
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                        )
                    )
            )
        }
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    scroll: ScrollState = rememberScrollState(0),
    data: MovieDetailModel?,
    imageData: List<Posters>?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(scroll)
            .padding(top = 20.dp)
    ) {
        Spacer(Modifier.height(headerHeight))
        Row {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(0.4F)
            ) {
                AsyncImage(

                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/original/${data?.posterPath}")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    contentDescription = "backdrop image",
                )
            }
            Column {
                modifier
                    .weight(0.6F)
                    .padding(horizontal = 10.dp)
                Text(
                    text = "${data?.title}",
                    style = bigTextStyle
                )
                Text(
                    text = "Release date: ${data?.releaseDate}",
                    style = mediumTextStyle,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "Runtime: ${data?.runtime} minutes",
                    style = mediumTextStyle,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "Popularity: ${data?.popularity}",
                    style = mediumTextStyle,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "Genre: ${
                        data?.genres?.joinToString {
                            "${it.name}, "
                        } ?: ""
                    }",
                    style = mediumTextStyle,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        LazyRow(
            Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            val count =
                if ((imageData?.size ?: 0) > 10) 10 else imageData?.size ?: 0
            items(count) { i ->
                AsyncImage(

                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/original/${imageData?.get(i)?.filePath}")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    contentDescription = "backdrop image",
                    modifier = Modifier
                        .size(75.dp)
                )
            }
        }
        Text(
            text = "${data?.overview}",
            style = bigTextStyle,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(16.dp)
        )
    }
}
