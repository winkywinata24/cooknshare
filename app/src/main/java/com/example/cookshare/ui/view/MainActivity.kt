package com.example.cookshare.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.cookshare.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        loadFragment(HomeFragment())

        val bottomNav : BottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                }
                R.id.search -> {
                    loadFragment(SearchFragment())
                }
                R.id.myRecipe -> {
                    loadFragment(MyRecipeFragment())
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                }
            }
            true
        }
    }
    private fun loadFragment (fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_bottomnav, fragment)
        transaction.commit()
    }
}