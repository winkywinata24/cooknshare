package com.example.cookshare.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cookshare.R

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref: SharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username: EditText = findViewById(R.id.editTextText2)
        val password: EditText = findViewById(R.id.editTextTextPassword)
        val btnLogin: Button = findViewById(R.id.button5)
        val btnRegis: TextView = findViewById(R.id.textView5)

        val usernameVal = sharedPref.getString("username", null)
        val passwordVal = sharedPref.getString("password", null)
        val isLogin = sharedPref.getString("isLogin", null)

        if (isLogin == "1") {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btnLogin.setOnClickListener {
            val a = username.text.toString()
            val b = password.text.toString()
            if(a == usernameVal && b == passwordVal) {
                val editor = sharedPref.edit()
                editor.putString("isLogin", "1")
                editor.apply()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Username atau Password Salah", Toast.LENGTH_LONG).show()
            }
        }

        btnRegis.setOnClickListener {
            val i = Intent(this, RegistActivity::class.java)
            startActivity(i)
        }
    }
}