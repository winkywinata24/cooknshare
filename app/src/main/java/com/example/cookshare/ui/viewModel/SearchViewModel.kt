package com.example.cookshare.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.utils.NetworkUtils
import com.example.cookshare.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModel(
    private val userFoodRepository: UserFoodRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<Resource<List<FoodItems>>>()
    val searchResults: LiveData<Resource<List<FoodItems>>> get() = _searchResults

    private val allFoodItems = mutableListOf<FoodItems>()

    fun performSearch(query: String) {
        _searchResults.value = Resource.Loading()

        if (query.isBlank()) {
            _searchResults.value = Resource.Success(allFoodItems)
        } else {
            val filteredResults = allFoodItems.filter {
                it.title.contains(query, ignoreCase = true)
            }

            if (filteredResults.isNotEmpty()) {
                _searchResults.value = Resource.Success(filteredResults)
            } else {
                _searchResults.value = Resource.Empty("No results found")
            }
        }
    }

    fun getFoodDataFromApi(context: Context, forceRefresh: Boolean = false) {
        if (_searchResults.value == null || forceRefresh) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                viewModelScope.launch {
                    try {
                        _searchResults.value = Resource.Loading()

                        // Ambil token dari repository
                        val token = userFoodRepository.tokenBearer
                        Log.d("SearchViewModel", "Token: $token")

                        val response = RetrofitInstance.getCrudApi().getAllFoods(token)

                        if (response.isSuccessful) {
                            val foodResponse = response.body()
                            if (foodResponse != null && foodResponse.items.isNotEmpty()) {
                                allFoodItems.clear()
                                allFoodItems.addAll(foodResponse.items)
                                _searchResults.value = Resource.Success(allFoodItems)
                            } else {
                                _searchResults.value = Resource.Empty("No data found")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string() ?: "Unknown error"
                            Log.e("SearchViewModel", "API Error: ${response.code()} - $errorBody")
                            _searchResults.value = Resource.Error("Failed to fetch data: ${response.code()} - $errorBody")
                        }
                    } catch (e: Exception) {
                        _searchResults.value = Resource.Error("Unknown error: ${e.message}")
                        Log.e("SearchViewModel", "Error: ${e.message}")
                    }
                }
            } else {
                _searchResults.value = Resource.Error("No internet connection")
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = Resource.Success(emptyList())
    }
}
