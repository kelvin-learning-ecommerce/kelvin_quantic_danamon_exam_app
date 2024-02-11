package com.kelvinquantic.danamon.ui.photolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kelvinquantic.danamon.model.PhotoUIModel
import com.kelvinquantic.danamon.ui.photolist.components.Content
import com.kelvinquantic.danamon.ui.photolist.components.HomeScreen
import com.kelvinquantic.danamon.ui.theme.DanamonAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DanamonAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    DanamonAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(
                data = listOf(
                    PhotoUIModel(
                        title = "accusamus beatae ad facilis cum similique qui sunt",
                        albumId = 1,
                        id = 1,
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    )
                ),
                onItemFav = {}
            )
        }
    }
}
