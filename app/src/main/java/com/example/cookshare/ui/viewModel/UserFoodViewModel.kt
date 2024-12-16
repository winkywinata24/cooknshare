package com.example.cookshare.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookshare.data.model.FoodPostRequest
import com.example.cookshare.data.model.FoodResponse
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.utils.NetworkUtils
import com.example.cookshare.utils.Resource
import kotlinx.coroutines.launch

class UserFoodViewModel (private val repository: UserFoodRepository) : ViewModel() {
    private val _data = MutableLiveData<Resource<FoodResponse>>()
    val data: LiveData<Resource<FoodResponse>> = _data

    private val _createStatus = MutableLiveData<Resource<Unit>>()
    val createStatus : LiveData<Resource<Unit>> = _createStatus

    private val _deleteStatus = MutableLiveData<Resource<Unit>>()
    val deleteStatus: LiveData<Resource<Unit>> get() = _deleteStatus

    fun getFoods(context: Context, forceRefresh: Boolean = false) {
        if (_data.value == null || forceRefresh) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                viewModelScope.launch {
                    try {
                        //delay(3000)
                        _data.value = Resource.Loading()
                        val response = repository.fetchUserFood()
                        Log.d("UserFoodViewModel", "Response items: ${response.items}")
                        if (response.items.isEmpty()) {
                            _data.postValue(Resource.Empty("No food found"))
                        } else {
                            _data.postValue(Resource.Success(response))
                        }
                    } catch (e: Exception) {
                        _data.postValue(Resource.Error("Unknown error: ${e.message}"))
                    }
                }
            } else {
                _data.postValue(Resource.Error("No internet connection"))
            }
        }
    }

    fun createFood(context: Context, food: List<FoodPostRequest>) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _createStatus.value = Resource.Loading()

                    val response = repository.createFood(food)
                    _createStatus.postValue(Resource.Success(Unit))

                    // Refresh data setelah create sukses
                    getFoods(context, forceRefresh = true)
                } catch (e: Exception) {
                    _createStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        } else {
            _createStatus.postValue(Resource.Error("No internet connection"))
        }
    }

    fun deleteFood(context: Context, foodId: String) {
        viewModelScope.launch {
            _deleteStatus.value = Resource.Loading()
            try {
                val response = repository.deleteFood(foodId)
                if (response.isSuccessful) {
                    _deleteStatus.value = Resource.Success(Unit)
                } else {
                    _deleteStatus.value = Resource.Error("Gagal menghapus data.")
                }
            } catch (e: Exception) {
                _deleteStatus.value = Resource.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}