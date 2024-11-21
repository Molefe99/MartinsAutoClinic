package com.example.martinsautoclinic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val btnRequestQuote = findViewById<Button>(R.id.btnRequestQuote)

        btnRequestQuote.setOnClickListener {
        val intent = Intent(this, RequestQuote::class.java)
        startActivity(intent)
    }

        val btnViewPricing = findViewById<Button>(R.id.btnViewPricing)

        btnViewPricing.setOnClickListener {

            val intent = Intent(this, ClientViewPricing::class.java)
            startActivity(intent)

        }
}
}
