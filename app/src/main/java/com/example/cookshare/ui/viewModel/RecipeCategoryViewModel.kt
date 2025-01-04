package com.example.cookshare.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.data.network.ApiService
import com.example.cookshare.data.repository.UserFoodRepository
import kotlinx.coroutines.launch

class RecipeCategoryViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val repository = UserFoodRepository(apiService)

    private val _recipes = MutableLiveData<List<FoodItems>>()
    val recipes: LiveData<List<FoodItems>> get() = _recipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchRecipesByCategory(category: String = "default") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            try {
                val foodResponse = apiService.getUserFood("Bearer yNLVSwgnpSpl6i4YgNv8gWHdwwOdeEf5jWsqsE0P8gd5UjHSlA")
                val filteredRecipes = foodResponse.items.filter { it.category == category }
                _recipes.value = filteredRecipes
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown Error"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
