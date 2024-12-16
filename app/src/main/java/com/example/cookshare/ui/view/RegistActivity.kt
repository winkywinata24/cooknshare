package com.example.cookshare.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cookshare.R
import com.google.android.material.snackbar.Snackbar

class RegistActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref: SharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username: EditText = findViewById(R.id.editTextText2)
        val password: EditText = findViewById(R.id.editTextTextPassword)
        val confirmpassword: EditText = findViewById(R.id.editTextTextConfirmPassword)
        val btnRegis: Button = findViewById(R.id.button5)
        val btnLogin: TextView = findViewById(R.id.textView5)

        btnRegis.setOnClickListener {
            val a = username.text.toString()
            val b = password.text.toString()
            val c = confirmpassword.text.toString()
            if (a.isNotEmpty() && b.isNotEmpty() && c.isNotEmpty() && b == c) {
                val editor = sharedPref.edit()
                editor.putString("username", a)
                editor.putString("password", b)
                editor.apply()
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                Toast.makeText(this, "Berhasil Registrasi", Toast.LENGTH_LONG).show()
            } else {
                showSnackBar("Gagal Registrasi")
            }
        }

        btnLogin.setOnClickListener {
            finish()
        }
    }
    private fun showSnackBar(message: String) {
        val view = this.findViewById<View>(android.R.id.content)
        val snacbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        snacbar.show()
    }
}