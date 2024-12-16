package com.example.cookshare.data.network

import com.example.cookshare.data.model.ApiResponse
import com.example.cookshare.data.model.FoodPostRequest
import com.example.cookshare.data.model.FoodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/complexSearch")
    suspend fun getFoods(@Query("apiKey") apiKey: String): Response<ApiResponse>

    @POST("food")
    suspend fun createUserFood(
        @Header("Authorization") token: String,
        @Body foods: List<FoodPostRequest>,
    ): FoodResponse

    @GET("food")
    suspend fun getUserFood(
        @Header("Authorization") token: String
    ): FoodResponse

    @DELETE("food/{id}")
    suspend fun deleteUserFood(
        @Header("Authorization") token: String,
        @Path("id") foodId: String
    ): Response<Unit>
}