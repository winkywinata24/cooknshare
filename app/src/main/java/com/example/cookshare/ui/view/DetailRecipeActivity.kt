package com.example.cookshare.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cookshare.R
import com.example.cookshare.data.model.FoodItems
import com.squareup.picasso.Picasso

class DetailRecipeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Ambil data dari Intent
        val foodItem: FoodItems? = intent.getParcelableExtra("EXTRA_FOOD_ITEM")

        // Sinkronisasi data dengan view
        foodItem?.let {
            findViewById<TextView>(R.id.recipe_name).text = it.title
            findViewById<TextView>(R.id.recipe_category).text = it.category

            // Format ingredients
            val ingredientsText = it.ingredients
                .mapIndexed { index, ingredient -> "${index + 1}. $ingredient" }
                .joinToString("\n")
            findViewById<TextView>(R.id.recipe_ingredients).text = ingredientsText

            // Format steps
            val stepsText = it.steps
                .mapIndexed { index, step -> "Langkah ${index + 1}: $step" }
                .joinToString("\n")
            findViewById<TextView>(R.id.recipe_steps).text = stepsText

            val imageView = findViewById<ImageView>(R.id.recipe_image)
            Picasso.get().load(it.image).into(imageView)
        }



        // Tombol kembali
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
    }
}