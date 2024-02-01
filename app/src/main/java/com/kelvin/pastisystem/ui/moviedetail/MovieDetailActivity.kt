package com.kelvin.pastisystem.ui.moviedetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kelvin.pastisystem.model.MovieDetailModel
import com.kelvin.pastisystem.model.MovieGenres
import com.kelvin.pastisystem.model.Posters
import com.kelvin.pastisystem.ui.moviedetail.components.Body
import com.kelvin.pastisystem.ui.moviedetail.components.CollapsingToolbar
import com.kelvin.pastisystem.ui.theme.LearningTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MovieDetailScreen()
                }
            }
        }
    }
}

@Composable
fun MovieDetailScreen() {
    CollapsingToolbar(
        Modifier
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun MovieDetailPreview() {
    LearningTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Body(
                data = MovieDetailModel(
                    overview = "Black Manta, still driven by the need to avenge his father's death and " +
                            "wielding the power of the mythic Black Trident, will stop at nothing to " +
                            "take Aquaman down once and for all. To defeat him, Aquaman must turn to " +
                            "his imprisoned brother Orm, the former King of Atlantis, to forge an " +
                            "unlikely alliance in order to save the world from irreversible destruction.",
                    title = "Aquaman and the Lost Kingdom",
                    releaseDate = "20-11-2023",
                    runtime = 100,
                    popularity = 100.toDouble(),
                    genres = arrayListOf(
                        MovieGenres(
                            name = "Action"
                        ),
                        MovieGenres(
                            name = "Fantasy"
                        )
                    )

                ), imageData = listOf(
                    Posters()
                )
            )
        }
    }
}


