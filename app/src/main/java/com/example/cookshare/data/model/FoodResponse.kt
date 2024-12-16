package com.example.cookshare.data.model

data class FoodResponse(
    val cursor: String,
    val items: List<FoodItems>,
    val next_page: String,
)
