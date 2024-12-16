package com.example.cookshare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.data.model.SearchModel
import com.example.cookshare.databinding.ItemSearchResultBinding

class SearchAdapter (private var results: List<SearchModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = results[position]
        with(holder.binding) {
            resultTextView.text = foodItem.name
            resultImageView.setImageResource(foodItem.imageResId)
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<SearchModel>) {
        results = newResults
        notifyDataSetChanged()
    }
}