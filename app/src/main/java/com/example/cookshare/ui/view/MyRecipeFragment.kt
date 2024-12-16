package com.example.cookshare.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookshare.data.model.FoodItems
import com.example.cookshare.data.model.FoodPostRequest
import com.example.cookshare.data.network.RetrofitInstance
import com.example.cookshare.data.repository.UserFoodRepository
import com.example.cookshare.databinding.FragmentMyRecipeBinding
import com.example.cookshare.databinding.BottomSheetLayoutBinding
import com.example.cookshare.ui.adapter.InputFoodAdapter
import com.example.cookshare.ui.adapter.UserFoodListAdapter
import com.example.cookshare.ui.viewModel.UserFoodViewModel
import com.example.cookshare.utils.Resource
import com.example.cookshare.utils.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MyRecipeFragment : Fragment() {
    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!

    private val userFoodViewModel: UserFoodViewModel by activityViewModels {
        ViewModelFactory(UserFoodViewModel::class.java) {
            val repository = UserFoodRepository(RetrofitInstance.getCrudApi())
            UserFoodViewModel(repository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRecipeBinding.inflate(inflater, container, false)

        setupFAB(binding)
        getUserFood()

        return binding.root
    }

    private fun getUserFood() {
        userFoodViewModel.getFoods(requireContext())
        userFoodViewModel.data.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {
                    Log.d("Data Food", "Data Kosong. (${resource.message})")
                    binding.emptyFood.root.visibility = View.VISIBLE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.GONE
                    binding.foodList.visibility = View.GONE

                    binding.emptyFood.emptyMessage.text = resource.message
                }

                is Resource.Error -> {
                    Log.e("Data Food", resource.message.toString())
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.VISIBLE
                    binding.foodList.visibility = View.GONE

                    binding.errorFood.errorMessage.text = resource.message

                    binding.errorFood.retryButton.setOnClickListener {
                        userFoodViewModel.getFoods(requireContext(), true)
                    }
                }

                is Resource.Loading -> {
                    Log.d("Data Food", "Mohon Tunggu...")
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.VISIBLE
                    binding.errorFood.root.visibility = View.GONE
                    binding.foodList.visibility = View.GONE
                }

                is Resource.Success -> {
                    Log.d("Data Food", "Data berhasil didapatkan")
                    binding.emptyFood.root.visibility = View.GONE
                    binding.loadingFood.root.visibility = View.GONE
                    binding.errorFood.root.visibility = View.GONE
                    binding.foodList.visibility = View.VISIBLE

                    val foodItems = resource.data!!.items.toMutableList()
                    val adapter = UserFoodListAdapter(
                        foodItems,
                        onDeleteClick = { foodItem ->
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Hapus Resep")
                                .setMessage("Apakah Anda yakin ingin menghapus resep ini?")
                                .setPositiveButton("Hapus") { _, _ ->
                                    deleteFood(foodItem)
                                }
                                .setNegativeButton("Batal", null)
                                .show()
                        },
                        onItemClick = { foodItem ->
                            val intent = Intent(requireContext(), DetailRecipeActivity::class.java)
                            intent.putExtra("EXTRA_FOOD_ITEM", foodItem)
                            startActivity(intent)
                        }
                    )
                    binding.foodList.adapter = adapter
                    binding.foodList.layoutManager = LinearLayoutManager(requireContext())

                }
            }
        }
    }

    private fun deleteFood(foodItem: FoodItems) {
        userFoodViewModel.deleteFood(requireContext(), foodItem._uuid)

        userFoodViewModel.deleteStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Snackbar.make(binding.root, "Menghapus...", Snackbar.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    // Show success message
                    Snackbar.make(binding.root, "Resep berhasil dihapus!", Snackbar.LENGTH_SHORT).show()

                    // Access the current list of food items from the adapter
                    val adapter = binding.foodList.adapter as? UserFoodListAdapter
                    val updatedFoodItems = adapter?.foodList?.filterNot { it._uuid == foodItem._uuid }

                    // Update the adapter with the filtered list
                    updatedFoodItems?.let {
                        adapter.updateItems(it)
                    }
                }

                is Resource.Error -> {
                    Snackbar.make(
                        binding.root,
                        resource.message ?: "Gagal menghapus resep.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                is Resource.Empty -> TODO()
            }
        }
    }


    private fun setupFAB(binding: FragmentMyRecipeBinding) {
        binding.fab.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val binding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()

        initTitleInput(binding)
        initImageInput(binding)
        initSingleChoiceDialog(binding)
        initIngredientsInput(binding)
        initStepsInput(binding)
        createFood(binding)
    }

    private fun getIngredients(binding: BottomSheetLayoutBinding): List<String> {
        val ingredientsAdapter = binding.ingredientsRecyclerView.adapter as? InputFoodAdapter
        return ingredientsAdapter?.getItems() ?: emptyList()
    }

    private fun getSteps(binding: BottomSheetLayoutBinding): List<String> {
        val stepsAdapter = binding.stepsRecyclerView.adapter as? InputFoodAdapter
        return stepsAdapter?.getItems() ?: emptyList()
    }

    private fun createFood(binding: BottomSheetLayoutBinding) {
        binding.btnSubmit.setOnClickListener {
            val foodPostRequest = listOf(
                FoodPostRequest(
                    title = binding.inputTitle.text.toString(),
                    image = binding.inputImage.text.toString(),
                    category = binding.inputCategory.text.toString(),
                    ingredients = getIngredients(binding),
                    steps = getSteps(binding)
                )
            )

            userFoodViewModel.createFood(requireContext(), foodPostRequest)
            userFoodViewModel.createStatus.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Show a loading indicator for create operation
                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Food created successfully!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        bottomSheetDialog.dismiss()
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            binding.root,
                            resource.message ?: "Failed to create food.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showSingleChoiceDialog(
        options: Array<String>,
        checkedItem: Int,
        onChoiceSelected: (Int) -> Unit
    ) {
        var tempSelectedOption = checkedItem
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Pilih Category")
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                tempSelectedOption = which
            }
            .setPositiveButton("Ok") { dialog, _ ->
                onChoiceSelected(tempSelectedOption)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun initTitleInput(binding: BottomSheetLayoutBinding) {
        binding.inputTitle.isFocusable = true
        binding.inputTitle.isFocusableInTouchMode = true
    }

    private fun initImageInput(binding: BottomSheetLayoutBinding) {
        binding.inputImage.isFocusable = true
        binding.inputImage.isFocusableInTouchMode = true
    }

    private fun initSingleChoiceDialog(binding: BottomSheetLayoutBinding) {
        val options =
            arrayOf("Main Dish", "Snacks", "Drink", "Dessert", "Traditional Food", "Healthy Food")
        val selectedOption = 0

        binding.inputCategory.setOnClickListener {
            showSingleChoiceDialog(options, selectedOption) { choice ->
                binding.inputCategory.setText(options[choice])
            }
        }
    }

    private fun initIngredientsInput(binding: BottomSheetLayoutBinding) {
        val adapter = InputFoodAdapter()
        binding.ingredientsRecyclerView.adapter = adapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddIngredient.setOnClickListener {
            val ingredient = binding.inputIngredient.text.toString()
            if (ingredient.isNotEmpty()) {
                adapter.addItem(ingredient)
                binding.inputIngredient.text?.clear()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Ingredient cannot be empty")
                    .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun initStepsInput(binding: BottomSheetLayoutBinding) {
        val adapter = InputFoodAdapter()
        binding.stepsRecyclerView.adapter = adapter
        binding.stepsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddStep.setOnClickListener {
            val step = binding.inputStep.text.toString()
            if (step.isNotEmpty()) {
                adapter.addItem(step)
                binding.inputStep.text?.clear()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Step cannot be empty")
                    .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

}