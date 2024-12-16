package com.example.cookshare.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.R
import com.example.cookshare.data.model.FoodItems
import com.squareup.picasso.Picasso

class UserFoodListAdapter (
    val foodList: MutableList<FoodItems>,
    private val onDeleteClick: (FoodItems) -> Unit,
    private val onItemClick: (FoodItems) -> Unit
) : RecyclerView.Adapter<UserFoodListAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.foodTitle)
        val categoryTextView: TextView = itemView.findViewById(R.id.foodCategory)
        val foodImageView: ImageView = itemView.findViewById(R.id.foodImage)
        val deleteButton: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.titleTextView.text = foodItem.title
        holder.categoryTextView.text = foodItem.category
        Picasso.get().load(foodItem.image).into(holder.foodImageView)
        holder.deleteButton.setOnClickListener {
            onDeleteClick(foodItem)
        }
        holder.itemView.setOnClickListener {
            onItemClick(foodItem)
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun updateItems(newItems: List<FoodItems>) {
        foodList.clear()
        foodList.addAll(newItems)
        notifyDataSetChanged()
    }
}