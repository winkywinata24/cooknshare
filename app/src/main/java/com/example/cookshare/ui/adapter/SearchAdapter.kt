package com.example.cookshare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.databinding.ItemSearchResultBinding
import com.squareup.picasso.Picasso
import android.content.Intent
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.ui.view.DetailRecipeActivity

class SearchAdapter(private var results: List<FoodItems>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodItems) {
            binding.foodTitle.text = item.title
            Picasso.get().load(item.image).into(binding.foodImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = results[position]
        holder.bind(item)

        // Set click listener to navigate to DetailRecipeActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailRecipeActivity::class.java)
            intent.putExtra("EXTRA_FOOD_ITEM", item) // Pass the complete FoodItems model
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<FoodItems>) {
        results = newResults
        notifyDataSetChanged()
    }
}