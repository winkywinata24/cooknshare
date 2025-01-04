package com.example.cookshare.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.data.model.SearchModel
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.databinding.FragmentSearchBinding
import com.example.cookshare.ui.adapter.SearchAdapter
import com.example.cookshare.ui.viewModel.SearchViewModel
import com.example.cookshare.utils.Resource
import com.example.cookshare.utils.ViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchResultsAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi UserFoodRepository
        val repository = UserFoodRepository(api = RetrofitInstance.getCrudApi())

        // Inisialisasi ViewModel dengan ViewModelFactory
        val factory = ViewModelFactory(SearchViewModel::class.java) { SearchViewModel(repository) }
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        // Panggil fungsi untuk mendapatkan data dari API
        viewModel.getFoodDataFromApi(requireContext())

        // Inisialisasi RecyclerView dan Adapter
        searchResultsAdapter = SearchAdapter(emptyList())
        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchResultsRecyclerView.adapter = searchResultsAdapter

        // Observasi Data dari ViewModel
        observeViewModel()

        // Listener untuk pencarian
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.performSearch(query)
                } else {
                    viewModel.clearSearchResults()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {
                    Log.d("Search", "Data Kosong. (${resource.message})")
                    binding.emptyFood.root.visibility = View.VISIBLE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.GONE
                    binding.searchResultsRecyclerView.visibility = View.GONE
                    binding.emptyFood.emptyMessage.text = resource.message
                }
                is Resource.Error -> {
                    Log.e("Search", resource.message.toString())
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.VISIBLE
                    binding.searchResultsRecyclerView.visibility = View.GONE
                    binding.errorFood.errorMessage.text = resource.message
                    binding.errorFood.retryButton.setOnClickListener {
                        viewModel.performSearch(binding.searchEditText.text.toString())
                    }
                }
                is Resource.Loading -> {
                    Log.d("Search", "Mohon Tunggu...")
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.VISIBLE
                    binding.errorFood.root.visibility = View.GONE
                    binding.searchResultsRecyclerView.visibility = View.GONE
                }
                is Resource.Success -> {
                    Log.d("Search", "Data berhasil didapatkan: ${resource.data}")
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.GONE
                    binding.searchResultsRecyclerView.visibility = View.VISIBLE

                    val foodItems = resource.data ?: emptyList()
                    if (foodItems.isEmpty()) {
                        binding.emptyFood.root.visibility = View.VISIBLE
                        binding.emptyFood.emptyMessage.text = "No results found"
                        binding.searchResultsRecyclerView.visibility = View.GONE
                    } else {
                        val searchResults = convertToSearchModel(foodItems)
                        Log.d("Search", "Data diteruskan ke adapter: $searchResults")
                        searchResultsAdapter.updateResults(searchResults)
                    }
                }

            }
        }
    }

    private fun convertToSearchModel(foodItems: List<FoodItems>): List<SearchModel> {
        return foodItems.map {
            SearchModel(
                title = it.title,
                image = it.image
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
