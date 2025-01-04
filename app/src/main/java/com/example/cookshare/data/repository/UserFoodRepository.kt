package com.example.cookshare.data.repository

import com.example.cookshare.data.model.FoodPostRequest
import com.example.cookshare.data.model.FoodResponse
import com.example.cookshare.data.network.ApiService
import retrofit2.Response

class UserFoodRepository(
    private val api: ApiService,
) {
    val tokenBearer = "Bearer yNLVSwgnpSpl6i4YgNv8gWHdwwOdeEf5jWsqsE0P8gd5UjHSlA"

    suspend fun fetchUserFood(): FoodResponse {
        return api.getUserFood(tokenBearer)
    }

    suspend fun createFood(foods: List<FoodPostRequest>): FoodResponse {
        return api.createUserFood(tokenBearer, foods)
    }

    suspend fun deleteFood(foodId: String): Response<Unit> {
        return api.deleteUserFood(tokenBearer, foodId)
    }
}