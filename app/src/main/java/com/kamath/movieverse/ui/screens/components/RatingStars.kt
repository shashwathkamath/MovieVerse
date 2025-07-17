package com.kamath.movieverse.ui.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RatingStars(rating: Double) {
    val stars = 5
    val filledStars = (rating / 2).toInt()  // Convert 10-scale to 5-star
    val halfStar = if (rating % 2 >= 1) 1 else 0

    Row {
        repeat(filledStars) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = Color.Yellow)
        }
        if (halfStar == 1) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Yellow)
        }
        repeat(stars - filledStars - halfStar) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Gray)
        }
    }
}

