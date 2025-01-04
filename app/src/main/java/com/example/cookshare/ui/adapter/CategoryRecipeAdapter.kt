package com.example.cookshare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.databinding.ItemFoodCategoryBinding
import com.squareup.picasso.Picasso

class CategoryRecipeAdapter(
    private val onItemClick: (FoodItems) -> Unit
) : ListAdapter<FoodItems, CategoryRecipeAdapter.RecipeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemFoodCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class RecipeViewHolder(private val binding: ItemFoodCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: FoodItems, onItemClick: (FoodItems) -> Unit) {
            binding.foodTitle.text = recipe.title
            binding.foodCategory.text = recipe.category
            Picasso.get().load(recipe.image).into(binding.foodImage)
            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodItems>() {
            override fun areItemsTheSame(oldItem: FoodItems, newItem: FoodItems): Boolean {
                return oldItem._uuid == newItem._uuid
            }

            override fun areContentsTheSame(oldItem: FoodItems, newItem: FoodItems): Boolean {
                return oldItem == newItem
            }
        }
    }
}