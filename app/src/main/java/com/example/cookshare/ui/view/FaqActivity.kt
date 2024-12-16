package com.example.cookshare.ui.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cookshare.R

class FaqActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faq)

        // Set up the WebView
        val webView: WebView = findViewById(R.id.faq_webview)

        // Enable JavaScript (if needed)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Set a WebViewClient to handle links inside the WebView
        webView.webViewClient = WebViewClient()

        // Load the FAQ URL
        webView.loadUrl("https://cookpad.com/id/faq?from=a")

        // Set up the Toolbar with back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Show back button
        supportActionBar?.setHomeButtonEnabled(true)      // Enable home button (back)

        // Handle edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Handle the back button click in the toolbar
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Navigate back to the previous activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
