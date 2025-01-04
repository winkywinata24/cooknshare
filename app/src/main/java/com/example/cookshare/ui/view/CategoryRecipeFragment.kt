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
import com.example.cookshare.data.network.ApiService
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.databinding.FragmentCategoryRecipeBinding
import com.example.cookshare.ui.adapter.CategoryRecipeAdapter
import com.example.cookshare.ui.viewModel.RecipeCategoryViewModel
import com.example.cookshare.utils.ViewModelFactory

class CategoryRecipeFragment : Fragment() {

    private var _binding: FragmentCategoryRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipeCategoryViewModel
    private lateinit var adapter: CategoryRecipeAdapter
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(category: String): CategoryRecipeFragment {
            val fragment = CategoryRecipeFragment()
            val bundle = Bundle()
            bundle.putString("category", category)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitInstance.getApiService("https://crudapi.co.uk/api/v1/")

        val viewModelFactory = ViewModelFactory(RecipeCategoryViewModel::class.java) {
            RecipeCategoryViewModel(apiService)
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeCategoryViewModel::class.java)
        adapter = CategoryRecipeAdapter{ foodItem ->
            val intent = Intent(requireContext(), DetailRecipeActivity::class.java).apply {
                putExtra("EXTRA_FOOD_ITEM", foodItem)
            }
            startActivity(intent)
        }

        binding.foodList.layoutManager = LinearLayoutManager(requireContext())
        binding.foodList.adapter = adapter

        observeViewModel()
        val category = arguments?.getString("category") ?: "default"
        viewModel.fetchRecipesByCategory(category)
    }

    private fun observeViewModel() {
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                binding.foodList.visibility = View.VISIBLE
                binding.emptyFood.root.visibility = View.GONE
                adapter.submitList(recipes)
            } else {
                binding.foodList.visibility = View.GONE
                binding.emptyFood.root.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingFood.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.errorFood.root.visibility = if (error.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
