package com.example.cookshare.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookshare.data.model.FoodResponse
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.utils.NetworkUtils
import com.example.cookshare.utils.Resource
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: UserFoodRepository) : ViewModel() {
    private val _data = MutableLiveData<Resource<FoodResponse>>()
    val data: LiveData<Resource<FoodResponse>> = _data

    fun getFoodDataFromApi(context: Context, forceRefresh: Boolean = false) {
        if (_data.value == null || forceRefresh) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                viewModelScope.launch {
                    try {
                        _data.value = Resource.Loading()

                        // Ambil token dari repository
                        val token = repository.tokenBearer
                        Log.d("FoodViewModel", "Token: $token")

                        val response = RetrofitInstance.getCrudApi().getAllFoods(token)

                        if (response.isSuccessful) {
                            val foodResponse = response.body()
                            if (foodResponse != null && foodResponse.items.isNotEmpty()) {
                                _data.value = Resource.Success(foodResponse)
                            } else {
                                _data.value = Resource.Empty("No data found")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string() ?: "Unknown error"
                            Log.e("FoodViewModel", "API Error: ${response.code()} - $errorBody")
                            _data.value =
                                Resource.Error("Failed to fetch data: ${response.code()} - $errorBody")
                        }
                    } catch (e: Exception) {
                        _data.value = Resource.Error("Unknown error: ${e.message}")
                        Log.e("FoodViewModel", "Error: ${e.message}")
                    }
                }
            } else {
                _data.value = Resource.Error("No internet connection")
            }
        }
    }
}