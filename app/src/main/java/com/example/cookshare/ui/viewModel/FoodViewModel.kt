package com.example.cookshare.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookshare.data.model.Food
import com.example.cookshare.data.repository.FoodRepository
import com.example.cookshare.utils.NetworkUtils
import com.example.cookshare.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FoodViewModel (private val repository: FoodRepository) : ViewModel() {
    private val _data = MutableLiveData<Resource<List<Food>>>()
    val data: LiveData<Resource<List<Food>>> = _data

    fun getFoods(context: Context, forceRefresh: Boolean = false) {
        if (_data.value == null || forceRefresh) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                viewModelScope.launch {
                    try {
                        _data.value = Resource.Loading()
                        delay(3000)
                        val apiKey = "a6d10e3066e24e57b49d0cfffb1d0945"
                        val response = repository.fetchFoods(apiKey)
                        if (response.isNullOrEmpty()) {
                            _data.postValue(Resource.Empty("No Data Found"))
                        } else {
                            _data.postValue(Resource.Success(response))
                        }
                    } catch (e: Exception) {
                        _data.postValue(Resource.Error("Unknown Error : ${e.message}"))
                    }
                }
            } else {
                _data.postValue(Resource.Error("No Internet Connection"))
            }
        }
    }
}