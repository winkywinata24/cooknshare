package com.example.cookshare.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsHorizontalModel(
    val title: String,
    val image: String,
    val category: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList()
) : Parcelable
