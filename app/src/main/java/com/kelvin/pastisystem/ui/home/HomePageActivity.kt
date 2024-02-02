package com.kelvin.pastisystem.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelvin.pastisystem.R
import com.kelvin.pastisystem.model.Genres
import com.kelvin.pastisystem.ui.common.ShimmerListItem
import com.kelvin.pastisystem.ui.home.viewmodel.HomeViewModel
import com.kelvin.pastisystem.ui.mlkit.MlActivity
import com.kelvin.pastisystem.ui.movielist.MovieListActivity
import com.kelvin.pastisystem.ui.theme.LearningTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HomePageScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(homeViewModel: HomeViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val state = homeViewModel.genreState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, MlActivity::class.java))
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
        content = {
            Column(
                modifier = Modifier.background(Color.White),
            ) {
                if (state.value.isLoading) {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(10) { i ->
                            ShimmerListItem()
                        }
                    }
                } else {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.value.genreList.size) { i ->
                            ProductCard(genreItem = state.value.genreList[i])
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    LearningTheme {
        ProductCard(
            genreItem = Genres(
                id = 10,
                name = "Action"
            )
        )
    }
}

@Composable
fun ProductCard(genreItem: Genres) {
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(),
        border = BorderStroke(width = 2.dp, Color.LightGray),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxSize()
            .background(Color.White)
            .clickable {
                val intent = Intent(context, MovieListActivity::class.java)
                intent.putExtra("id", genreItem.id)
                context.startActivity(intent)
            }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = genreItem.name ?: "",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
