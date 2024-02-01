package com.kelvin.pastisystem.ui.moviedetail.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kelvin.pastisystem.ui.theme.LearningTheme

@Composable
fun MovieDetailShimmerListItem() {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .shimmerBackground(RoundedCornerShape(4.dp))
                .fillMaxWidth()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 150.dp)
                        .shimmerBackground(RoundedCornerShape(4.dp))
                )

            }
            Column(Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 20.dp)
                        .shimmerBackground(RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(100.dp)
                        .shimmerBackground(RoundedCornerShape(4.dp))
                        .padding(top = 20.dp)
                )
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 20.dp)
                        .shimmerBackground(RoundedCornerShape(4.dp))
                        .padding(top = 20.dp)
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 20.dp)
                .shimmerBackground(RoundedCornerShape(4.dp))
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun ShimmerListItem_Preview() {
    LearningTheme {
        MovieDetailShimmerListItem()
    }
}
