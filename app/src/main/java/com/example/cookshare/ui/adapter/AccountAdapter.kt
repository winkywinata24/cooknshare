package com.example.cookshare.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.cookshare.data.model.AccountModel
import com.example.cookshare.databinding.ListMenuBinding

class AccountAdapter (
    context: Context,
    private val menuList: List<AccountModel>
) : ArrayAdapter<AccountModel>(context, 0, menuList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListMenuBinding
        if (convertView == null) {
            binding = ListMenuBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ListMenuBinding
        }

        val menuItem = getItem(position)
        menuItem?.let {
            binding.imageView.setImageResource(it.imageResId)
            binding.textName.text = it.name
        }

        return binding.root
    }
}