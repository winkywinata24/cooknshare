//package com.example.cookshare.ui.view
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.cookshare.data.network.RetrofitInstance
//import com.example.cookshare.data.repository.UserFoodRepository
//import com.example.cookshare.databinding.FragmentMyRecipeBinding
//import com.example.cookshare.ui.adapter.FoodCategoryAdapter
//import com.example.cookshare.ui.viewModel.UserFoodViewModel
//import com.example.cookshare.utils.Resource
//import com.example.cookshare.utils.ViewModelFactory
//
//class CategoryFragment : Fragment() {
//    private var _binding: FragmentMyRecipeBinding? = null
//    private val binding get() = _binding!!
//
//    private val userFoodViewModel: UserFoodViewModel by activityViewModels {
//        ViewModelFactory(UserFoodViewModel::class.java) {
//            val repository = UserFoodRepository(RetrofitInstance.getCrudApi())
//            UserFoodViewModel(repository)
//        }
//    }
//
//    private lateinit var selectedCategory: String // Untuk menyimpan kategori yang dipilih
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentMyRecipeBinding.inflate(inflater, container, false)
//
//        // Mendapatkan kategori yang dipilih dari argumen atau intent
//        arguments?.let {
//            selectedCategory = it.getString("CATEGORY", "")
//        }
//
//        getCategoryFood()
//
//        return binding.root
//    }
//
//    private fun getCategoryFood() {
//        // Mengambil makanan berdasarkan kategori
//        userFoodViewModel.getFoodsByCategory(requireContext(), selectedCategory)
//
//        userFoodViewModel.data.observe(viewLifecycleOwner) { resource ->
//            when (resource) {
//                is Resource.Empty -> {
//                    binding.emptyFood.root.visibility = View.VISIBLE
//                    binding.loadingFood.root.visibility = View.GONE
//                    binding.errorFood.root.visibility = View.GONE
//                    binding.foodList.visibility = View.GONE
//
//                    binding.emptyFood.emptyMessage.text = resource.message
//                }
//
//                is Resource.Error -> {
//                    binding.emptyFood.root.visibility = View.GONE
//                    binding.loadingFood.root.visibility = View.GONE
//                    binding.errorFood.root.visibility = View.VISIBLE
//                    binding.foodList.visibility = View.GONE
//
//                    binding.errorFood.errorMessage.text = resource.message
//                }
//
//                is Resource.Loading -> {
//                    binding.emptyFood.root.visibility = View.GONE
//                    binding.loadingFood.root.visibility = View.VISIBLE
//                    binding.errorFood.root.visibility = View.GONE
//                    binding.foodList.visibility = View.GONE
//                }
//
//                is Resource.Success -> {
//                    binding.emptyFood.root.visibility = View.GONE
//                    binding.loadingFood.root.visibility = View.GONE
//                    binding.errorFood.root.visibility = View.GONE
//                    binding.foodList.visibility = View.VISIBLE
//
//                    val foodItems = resource.data!!.items.filter { it.category == selectedCategory }.toMutableList()
//                    val adapter = UserFoodListAdapter(
//                        foodItems,
//                        onDeleteClick = { foodItem -> },
//                        onItemClick = { foodItem ->
//                            val intent = Intent(requireContext(), DetailRecipeActivity::class.java)
//                            intent.putExtra("EXTRA_FOOD_ITEM", foodItem)
//                            startActivity(intent)
//                        }
//                    )
//                    binding.foodList.adapter = adapter
//                    binding.foodList.layoutManager = LinearLayoutManager(requireContext())
//                }
//            }
//        }
//    }
//}
