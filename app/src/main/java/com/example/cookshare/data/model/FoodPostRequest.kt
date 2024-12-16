package com.example.cookshare.data.model

data class FoodPostRequest(
    val title: String,
    val image: String,
    val category: String,
    val ingredients: List<String>,
    val steps: List<String>
)
