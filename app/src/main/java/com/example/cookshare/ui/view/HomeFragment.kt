package com.example.cookshare.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookshare.R
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.data.repository.FoodRepository
import com.example.cookshare.databinding.FragmentHomeBinding
import com.example.cookshare.data.model.NewsHorizontalModel
import com.example.cookshare.ui.adapter.AutoSliderAdapter
import com.example.cookshare.ui.adapter.NewsHorizontalAdapter
import com.example.cookshare.ui.viewModel.FoodViewModel
import com.example.cookshare.utils.Resource
import com.example.cookshare.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val foodViewModel: FoodViewModel by lazy {
        val repository = FoodRepository(RetrofitInstance.getSpoonacularApi())
        ViewModelProvider(
            this,
            ViewModelFactory(FoodViewModel::class.java) { FoodViewModel(repository) })[FoodViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupGridMenu(binding)
        setupNewsHorizontal(binding)
        setupAutoSlider(binding)
        return binding.root
    }

    private fun setupAutoSlider(binding: FragmentHomeBinding) {
        val images = listOf(
            R.drawable.food_1,
            R.drawable.food_2,
            R.drawable.food_3
        )
        binding.autoSlider.adapter = AutoSliderAdapter(images, binding.autoSlider)
        binding.wornIndicator.attachTo(binding.autoSlider)
    }

    private fun setupGridMenu(binding: FragmentHomeBinding) {
        binding.includeHomeMenuGrid.menu1.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 1", Toast.LENGTH_LONG).show()
        }
        binding.includeHomeMenuGrid.menu2.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 2", Toast.LENGTH_LONG).show()
        }
        binding.includeHomeMenuGrid.menu3.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 3", Toast.LENGTH_LONG).show()
        }
        binding.includeHomeMenuGrid.menu4.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 4", Toast.LENGTH_LONG).show()
        }
        binding.includeHomeMenuGrid.menu5.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 5", Toast.LENGTH_LONG).show()
        }
        binding.includeHomeMenuGrid.menu6.setOnClickListener {
            Toast.makeText(context, "Anda Klik Menu 6", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupNewsHorizontal(binding: FragmentHomeBinding) {
        val adapter = NewsHorizontalAdapter(emptyList())

        foodViewModel.getFoods(requireContext())
        foodViewModel.data.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {
                    binding.emptyNewHoriList.root.visibility = View.VISIBLE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.GONE
                    binding.emptyNewHoriList.emptyMessage.text = resource.message
                    Log.d("Food Data", "Data Kosong. (${resource.message})")
                }

                is Resource.Error -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.VISIBLE
                    binding.newHoriList.visibility = View.GONE
                    binding.errorNewHoriList.errorMessage.text = resource.message
                    binding.errorNewHoriList.retryButton.setOnClickListener {
                        foodViewModel.getFoods(requireContext(), true)
                    }
                    Log.d("Food Data", resource.message.toString())
                }

                is Resource.Loading -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.VISIBLE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.GONE
                    Log.d("Food Data", "Mohon Tunggu...")
                }

                is Resource.Success -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.VISIBLE
                    Log.d("Food Data", "Data berhasil didapatkan")
                    val newsItem = resource.data!!.mapIndexed { index, data ->
                        NewsHorizontalModel(
                            data.image, data.title
                        )
                    }
                    adapter.updateData(newsItem)
                }
            }
        }

        binding.newHoriList.adapter = adapter
        binding.newHoriList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }
}