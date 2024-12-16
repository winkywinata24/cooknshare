package com.example.cookshare.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookshare.R
import com.example.cookshare.databinding.FragmentSearchBinding
import com.example.cookshare.ui.adapter.SearchAdapter
import com.example.cookshare.data.model.SearchModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchResultsAdapter: SearchAdapter

    private val sampleData = listOf(
        SearchModel("Nasi Goreng", R.drawable.nasi_goreng),
        SearchModel("Mie Goreng", R.drawable.mie_goreng),
        SearchModel("Pasta", R.drawable.pasta),
        SearchModel("Rendang", R.drawable.rendang),
        SearchModel("Ayam Goreng", R.drawable.ayam_goreng)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        searchResultsAdapter = SearchAdapter(emptyList())
        binding.searchResultsRecyclerView.adapter = searchResultsAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            searchResultsAdapter.updateResults(emptyList())
            return
        }

        val filteredResults = sampleData.filter { it.name.contains(query, ignoreCase = true) }
        searchResultsAdapter.updateResults(filteredResults)
    }
}