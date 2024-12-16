package com.example.cookshare.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookshare.data.model.NewsHorizontalModel
import com.example.cookshare.databinding.HomeHorizontalItemBinding
import com.squareup.picasso.Picasso

class NewsHorizontalAdapter (
    private var mList: List<NewsHorizontalModel>,
) : RecyclerView.Adapter<NewsHorizontalAdapter.ViewHolder>() {

    fun updateData(newItems: List<NewsHorizontalModel>) {
        mList = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        holder.binding.newsHoriTitle.text = item.newsTitle
        Picasso.get().load(item.imageUrl).into(holder.binding.newsHoriImage)
        holder.itemView.setOnClickListener {
            Log.i("RecyclerView", "Anda klik item ke $position : ${item.newsTitle}")
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(val binding: HomeHorizontalItemBinding) : RecyclerView.ViewHolder(binding.root)
}