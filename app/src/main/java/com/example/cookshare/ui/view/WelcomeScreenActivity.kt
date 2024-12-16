package com.example.cookshare.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.cookshare.R
import com.example.cookshare.ui.adapter.PagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var dotsIndicator : DotsIndicator
    private lateinit var btnSkip : Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref: SharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val isLogin = sharedPref.getString("isLogin", null)

        if (isLogin == "1") {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        viewPager = findViewById(R.id.viewPager)
        dotsIndicator = findViewById(R.id.dot_indicator)
        btnSkip = findViewById(R.id.button10)

        val fragmentList = listOf(Welcome1Fragment(), Welcome2Fragment(), Welcome3Fragment())
        val adapter = PagerAdapter(this, fragmentList)
        viewPager.adapter = adapter

        dotsIndicator.attachTo(viewPager)

        btnSkip.setOnClickListener {
            finishWelcomeScreen()
        }

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == fragmentList.size - 1) {
                    btnSkip.text = "Finish"
                } else {
                    btnSkip.text = "Skip"
                }
            }
        })
    }
    private fun finishWelcomeScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}