package com.kelvinquantic.danamon.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kelvinquantic.danamon.R
import com.kelvinquantic.danamon.model.PhotoUIModel
import com.kelvinquantic.danamon.utils.bigTextStyle


@Composable
fun PhotoListItem(
    data: PhotoUIModel,
    onItemClick: (PhotoUIModel) -> Unit,
    isFavoritePage: Boolean,
    onItemFav: (PhotoUIModel) -> Unit,
) {
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
                onItemClick(data)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
            ) {

                AsyncImage(

                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${data.thumbnailUrl}")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
                if (!isFavoritePage) {
                    Image(painter = painterResource(
                        if (!data.isFavorite) {R.drawable.ic_love}
                        else {
                            R.drawable.ic_love_red}
                    ), "Favorite icon",
                        modifier = Modifier
                            .clickable {
                                onItemFav(data)
                            }
                            .align(Alignment.TopEnd)
                            .padding(15.dp)
                            .size(30.dp))
                }

            }
            Text(
                text = data.title ?: "",
                style = bigTextStyle,
            )
        }
    }
}
