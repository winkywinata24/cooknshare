package com.example.cookshare.data.repository

import com.example.cookshare.data.model.Food
import com.example.cookshare.data.network.ApiService

class FoodRepository (private val apiService: ApiService) {
    suspend fun fetchFoods(apiKey: String): List<Food>? {
        val response = apiService.getFoods(apiKey)
        if (response.isSuccessful) {
            return response.body()?.results
        } else {
            return null
        }
    }
}