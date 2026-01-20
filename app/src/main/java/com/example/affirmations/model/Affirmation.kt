package com.example.affirmations.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Affirmation(
    @param:StringRes
    val stringResourceId: Int,
    @param:DrawableRes
    val imageResourceId: Int
)

data class AffirmationWithImage(
    val text: String,
    val imageUrl: String
)

