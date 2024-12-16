package com.example.cookshare.data.model

data class Food(
    val id: Int,
    val title: String,
    val image: String
)

data class ApiResponse(
    val results: List<Food>
)
