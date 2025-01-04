package com.example.cookshare.ui.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookshare.R
import com.example.cookshare.data.model.AccountModel
import com.example.cookshare.databinding.FragmentAccountBinding
import com.example.cookshare.ui.adapter.AccountAdapter

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        val usernameTextView: TextView = binding.textView10
        usernameTextView.text = username

        val menuList = listOf(
            AccountModel("FAQ", R.drawable.ic_faq),
            AccountModel("PRIVACY POLICY", R.drawable.ic_privacy),
            AccountModel("LOGOUT", R.drawable.ic_logout),
        )

        val adapter = AccountAdapter(requireContext(), menuList)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = menuList[position]

            when (selectedItem.name) {
                "FAQ" -> {
                    Toast.makeText(requireContext(), "Membuka Halaman FAQ", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(activity, FaqActivity::class.java)
                    startActivity(intent)
                }

                "PRIVACY POLICY" -> {
                    Toast.makeText(requireContext(), "Membuka Halaman PRIVACY", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(activity, PrivacyActivity::class.java)
                    startActivity(intent)
                }

                "LOGOUT" -> {
                    val sharedPref = requireActivity().getSharedPreferences(
                        "userPrefs",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = sharedPref.edit()
                    editor.putString("isLogin", "0")
                    editor.apply()

                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}