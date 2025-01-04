package com.example.cookshare.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.R
import com.example.cookshare.data.model.SearchModel
import com.example.cookshare.databinding.ItemSearchResultBinding
import com.squareup.picasso.Picasso

class SearchAdapter(private var results: List<SearchModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchModel) {
            binding.foodTitle.text = item.title
            Picasso.get()
                .load(item.image)
                .into(binding.foodImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<SearchModel>) {
        Log.d("SearchAdapter", "updateResults called with data: $newResults")
        results = newResults
        notifyDataSetChanged()
    }
}
