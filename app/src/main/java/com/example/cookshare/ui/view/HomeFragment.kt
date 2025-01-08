package com.example.cookshare.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookshare.R
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.databinding.FragmentHomeBinding
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.ui.adapter.AutoSliderAdapter
import com.example.cookshare.ui.adapter.NewsHorizontalAdapter
import com.example.cookshare.ui.viewModel.FoodViewModel
import com.example.cookshare.utils.Resource
import com.example.cookshare.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val foodViewModel: FoodViewModel by lazy {
        val repository =
            UserFoodRepository(RetrofitInstance.getSpoonacularApi()) // Use UserFoodRepository here
        ViewModelProvider(
            this,
            ViewModelFactory(FoodViewModel::class.java) { FoodViewModel(repository) }
        )[FoodViewModel::class.java]
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
            navigateToCategory("Main Dish")
        }
        binding.includeHomeMenuGrid.menu2.setOnClickListener {
            navigateToCategory("Snacks")
        }
        binding.includeHomeMenuGrid.menu3.setOnClickListener {
            navigateToCategory("Drink")
        }
        binding.includeHomeMenuGrid.menu4.setOnClickListener {
            navigateToCategory("Dessert")
        }
        binding.includeHomeMenuGrid.menu5.setOnClickListener {
            navigateToCategory("Traditional Food")
        }
        binding.includeHomeMenuGrid.menu6.setOnClickListener {
            navigateToCategory("Healthy Food")
        }
    }

    private fun navigateToCategory(category: String) {
        val fragment = CategoryRecipeFragment.newInstance(category)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottomnav, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupNewsHorizontal(binding: FragmentHomeBinding) {
        val adapter =
            NewsHorizontalAdapter(emptyList()) { foodItem -> // Ganti NewsHorizontalModel dengan FoodItems
                // Pindah ke DetailRecipeActivity saat item diklik
                val intent = Intent(requireContext(), DetailRecipeActivity::class.java)
                intent.putExtra("EXTRA_FOOD_ITEM", foodItem) // Kirimkan objek FoodItems
                startActivity(intent)
            }

        binding.newHoriList.adapter = adapter
        binding.newHoriList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        foodViewModel.getFoodDataFromApi(requireContext(), forceRefresh = false)

        foodViewModel.data.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {
                    binding.emptyNewHoriList.root.visibility = View.VISIBLE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.GONE
                    binding.emptyNewHoriList.emptyMessage.text = resource.message
                }

                is Resource.Error -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.VISIBLE
                    binding.newHoriList.visibility = View.GONE
                    binding.errorNewHoriList.errorMessage.text = resource.message
                }

                is Resource.Loading -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.VISIBLE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.emptyNewHoriList.root.visibility = View.GONE
                    binding.loadingNewHoriList.root.visibility = View.GONE
                    binding.errorNewHoriList.root.visibility = View.GONE
                    binding.newHoriList.visibility = View.VISIBLE

                    val foodItems = resource.data?.items ?: emptyList()
                    adapter.updateData(foodItems.map { foodItem ->
                        FoodItems(
                            _created = foodItem._created,
                            _data_type = foodItem._data_type,
                            _is_deleted = foodItem._is_deleted,
                            _modified = foodItem._modified,
                            _self_link = foodItem._self_link,
                            _user = foodItem._user,
                            _uuid = foodItem._uuid,
                            title = foodItem.title,
                            image = foodItem.image,
                            category = foodItem.category,
                            ingredients = foodItem.ingredients,
                            steps = foodItem.steps
                        )
                    })
                }
            }
        }
    }
}