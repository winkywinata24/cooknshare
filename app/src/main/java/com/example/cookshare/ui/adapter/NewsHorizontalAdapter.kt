package com.example.cookshare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.databinding.HomeHorizontalItemBinding
import com.squareup.picasso.Picasso
import com.example.cookshare.data.model.FoodItems

class NewsHorizontalAdapter(
    private var mList: List<FoodItems>, // Menggunakan FoodItems langsung
    private val onItemClick: (FoodItems) -> Unit // Listener untuk klik item
) : RecyclerView.Adapter<NewsHorizontalAdapter.ViewHolder>() {

    // Fungsi untuk memperbarui data
    fun updateData(newsItems: List<FoodItems>) {
        mList = newsItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        holder.binding.newsHoriTitle.text = item.title
        Picasso.get().load(item.image).into(holder.binding.newsHoriImage)

        holder.itemView.setOnClickListener {
            onItemClick(item) // Panggil listener ketika item diklik
        }
    }

    override fun getItemCount(): Int = mList.size

    class ViewHolder(val binding: HomeHorizontalItemBinding) : RecyclerView.ViewHolder(binding.root)
}
