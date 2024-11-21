package com.example.martinsautoclinic;


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        val backButton: Button = findViewById(R.id.back_button)

        val recyclerView: RecyclerView = findViewById(R.id.quotesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        val quotes = dbHelper.fetchQuotes()

        val adapter = QuotesAdapter(quotes)
        recyclerView.adapter = adapter

        // Handle Back Button Click
        backButton.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish() // Optional: closes the current activity
        }
    }
}

