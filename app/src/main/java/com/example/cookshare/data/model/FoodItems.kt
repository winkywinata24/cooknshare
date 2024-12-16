package com.example.cookshare.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItems(
    val _created: Double,
    val _data_type: String,
    val _is_deleted: Boolean,
    val _modified: Double,
    val _self_link: String,
    val _user: String,
    val _uuid: String,
    val title: String,
    val image: String,
    val category: String,
    val ingredients: List<String>,
    val steps: List<String>
) : Parcelable
